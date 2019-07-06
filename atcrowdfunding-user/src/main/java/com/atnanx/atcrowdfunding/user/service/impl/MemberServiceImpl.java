package com.atnanx.atcrowdfunding.user.service.impl;

import com.atnanx.atcrowdfunding.core.bean.TMember;
import com.atnanx.atcrowdfunding.core.bean.TMemberExample;
import com.atnanx.atcrowdfunding.core.bean.TMemberPhoneCode;
import com.atnanx.atcrowdfunding.core.common.ResponseCode;
import com.atnanx.atcrowdfunding.core.common.ServerResponse;
import com.atnanx.atcrowdfunding.core.constant.Const;
import com.atnanx.atcrowdfunding.core.constant.state.AcctTypeEnum;
import com.atnanx.atcrowdfunding.core.constant.state.AuthStatusEnum;
import com.atnanx.atcrowdfunding.core.constant.state.MessageTemplateEnum;
import com.atnanx.atcrowdfunding.core.constant.state.UserTypeEnum;
import com.atnanx.atcrowdfunding.core.util.AppendSensitiveUtil;
import com.atnanx.atcrowdfunding.core.util.JsonUtil;
import com.atnanx.atcrowdfunding.core.util.RandomDataUtil;
import com.atnanx.atcrowdfunding.core.vo.req.member.MemberRegisterReqVo;
import com.atnanx.atcrowdfunding.core.vo.resp.member.MemberLoginRespVo;
import com.atnanx.atcrowdfunding.user.component.AliSmsTemplate;
import com.atnanx.atcrowdfunding.user.mapper.TMemberMapper;
import com.atnanx.atcrowdfunding.user.mapper.TMemberPhoneCodeMapper;
import com.atnanx.atcrowdfunding.user.service.IMemberService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class MemberServiceImpl implements IMemberService {

    @Autowired
    private AliSmsTemplate aliSmsTemplate;

    @Autowired
    private TMemberMapper memberMapper;

    @Autowired
    private TMemberPhoneCodeMapper memberPhoneCodeMapper;

    @Autowired
    StringRedisTemplate stringRedisTemplate;


    @Override
    public ServerResponse<TMember> checkMemberExist(String username) {
        TMemberExample memberExample = new TMemberExample();
        TMemberExample.Criteria criteria = memberExample.createCriteria();
        criteria.andLoginacctEqualTo(username);
        List<TMember> tMembers = memberMapper.selectByExample(memberExample);
        if (tMembers.size()==0){
            return ServerResponse.createByErrorMessage("此用户不存在");
        } else if (tMembers.size()>1){
            log.error("数据库存在多个{}",username);
            return ServerResponse.createByErrorMessage("数据库异常");
        }

        return ServerResponse.createBySuccess("检验成功，该用户存在",tMembers.get(0));
    }


    @Override
    public ServerResponse login(String username, String password) {
        if (StringUtils.isBlank(username)){
            return ServerResponse.createByErrorMessage("用户名未传");
        } else if (StringUtils.isBlank(password)){
            return ServerResponse.createByErrorMessage("未传密码");
        }

        ServerResponse<TMember> memberServerResponse = checkMemberExist(username);

        TMember member = memberServerResponse.getData();

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean matches = passwordEncoder.matches(password, member.getUserpswd());

        if (!matches){
            return ServerResponse.createByErrorMessage("密码错误");
        }
        //不做用户只能单端登录了，好像市面上的产品都可以多端登录
        //封装响应memberVo信息给前端
        //将以登录用户存入redis中
        String loginMemberToken = UUID.randomUUID().toString().replace("-", "");

        MemberLoginRespVo memberLoginVo = assemMemberLoginVo(member,loginMemberToken);


        //ValueOperations
        stringRedisTemplate.opsForValue().set(Const.memberInfo.REDIS_LOGIN_MEMBER_PREFIX+loginMemberToken,
                JsonUtil.obj2Json(member),Const.memberInfo.MEMBER_REDIS_SESSION_EXTIME, TimeUnit.MINUTES);

        return ServerResponse.createBySuccess("登录成功",memberLoginVo);
    }

    public MemberLoginRespVo assemMemberLoginVo(TMember memberDto, String loginMemberToken){
        MemberLoginRespVo memberLoginVo = new MemberLoginRespVo();
        BeanUtils.copyProperties(memberDto,memberLoginVo);


        memberLoginVo.setRealname(AppendSensitiveUtil.reName(memberDto.getRealname()));
        memberLoginVo.setCardnum(AppendSensitiveUtil.reNo(memberDto.getCardnum()));

        memberLoginVo.setAccessToken(loginMemberToken);
        return memberLoginVo;
    }

    @Override
    public ServerResponse register(MemberRegisterReqVo memberRegisterVo) {
        String mobile = memberRegisterVo.getMobile();
        String email = memberRegisterVo.getEmail();
        String password = memberRegisterVo.getPassword();
        String code = memberRegisterVo.getCode();

        if (memberRegisterVo == null) {
            ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEAGAL_ARGUMENT.getCode(),ResponseCode.ILLEAGAL_ARGUMENT.getDesc());
        }
        if (StringUtils.isBlank(mobile)||StringUtils.isBlank(email)||StringUtils.isBlank(code)||StringUtils.isBlank(password)){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEAGAL_ARGUMENT.getCode(),ResponseCode.ILLEAGAL_ARGUMENT.getDesc());
        }

        if (!checkMobile(mobile)){
            log.info(mobile+"已经存在");
            return ServerResponse.createByErrorMessage(mobile+"已经注册");
        }

        if (!checkEmail(email)){
            log.info(email+"已经存在");
            return ServerResponse.createByErrorMessage(email+"已经注册");
        }

        String phoneCode = stringRedisTemplate.opsForValue().get(Const.memberInfo.REDIS_REGISTER_CODE_PREFIX + mobile);
        if (StringUtils.isBlank(phoneCode)){
            return ServerResponse.createByErrorMessage("验证码已失效,请重新获取");
        }

        if (!StringUtils.equals(code,phoneCode)){
            return ServerResponse.createByErrorMessage("验证码不正确，请重新输入");
        }

        TMember registerMember = assemRegisterMember(memberRegisterVo);
        int insertCount = memberMapper.insert(registerMember);
        if (insertCount<=0){
            return ServerResponse.createByErrorMessage("注册失败");
        }


        return ServerResponse.createBySuccessMessage("注册`成功");
    }

    public TMember assemRegisterMember(MemberRegisterReqVo memberRegisterVo){
        TMember registerMember = new TMember();
        registerMember.setLoginacct(memberRegisterVo.getMobile());

        //该加密算法盐值随机生成
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodePwd = encoder.encode(memberRegisterVo.getPassword());
        registerMember.setUserpswd(encodePwd);

        registerMember.setUsername(UUID.randomUUID().toString().replace("-","").substring(0,6));
        registerMember.setEmail(memberRegisterVo.getEmail());
        registerMember.setAuthstatus(AuthStatusEnum.UNAUTH.getCode());
        registerMember.setUsertype(UserTypeEnum.PERSONAL.getCode());
        registerMember.setRealname(null);
        registerMember.setCardnum(null);
        registerMember.setAccttype(AcctTypeEnum.PERSON.getCode());

        return registerMember;
    }

    @Override
    public ServerResponse getMemberInfo(String accessToken) {
        String memberStr = stringRedisTemplate.opsForValue().get(Const.memberInfo.REDIS_LOGIN_MEMBER_PREFIX + accessToken);

        if (StringUtils.isBlank(memberStr)){
            return ServerResponse.createByErrorMessage("请重新登录");
        }
        TMember member = JsonUtil.Json2Obj(memberStr, TMember.class);
        if (member==null){
            return ServerResponse.createByErrorMessage("获取失败");
        }
        String loginToken = UUID.randomUUID().toString().replace("-", "");
        MemberLoginRespVo memberLoginVo = assemMemberLoginVo(member,loginToken);

        //不知道这个有什么意义

        return ServerResponse.createBySuccess("获取用户信息成功",memberLoginVo);
    }

    @Override
    public ServerResponse loginByCode(String loginAccout, String verificationCode) {

        ServerResponse<TMember> memberServerResponse = checkMemberExist(loginAccout);

        String code = stringRedisTemplate.opsForValue().get(Const.memberInfo.REDIS_LOGIN_MEMBER_PREFIX + loginAccout);

        if (StringUtils.isBlank(verificationCode)){
            return ServerResponse.createByErrorMessage("验证码已失效,请重新获取");
        }

        if (!StringUtils.equals(code,verificationCode)){
            return ServerResponse.createByErrorMessage("验证码不正确，请重新输入");
        }

        TMember member = memberServerResponse.getData();

        String loginToken = UUID.randomUUID().toString().replace("-", "");
        MemberLoginRespVo memberLoginVo = assemMemberLoginVo(member,loginToken);

        //登录成功把用户信息存储到redis中
        //ValueOperations
        stringRedisTemplate.opsForValue().set(Const.memberInfo.REDIS_LOGIN_MEMBER_PREFIX+loginToken,
                JsonUtil.obj2Json(member),Const.memberInfo.MEMBER_REDIS_SESSION_EXTIME, TimeUnit.MINUTES);


        return ServerResponse.createBySuccess("登录成功",memberLoginVo);
    }

    @Override
    public ServerResponse sendSms(String mobile, String type) {


        String redisCodePrefix = MessageTemplateEnum.getRedisCodePrefix(type);


        Long expire = stringRedisTemplate.getExpire(redisCodePrefix+ mobile, TimeUnit.MINUTES);
        if (expire>=Const.memberInfo.CODE_SESSION_EXTIME-1&&expire<=Const.memberInfo.CODE_SESSION_EXTIME){
            log.info("{}手机发送短信1分钟不能超过1次",mobile);
            return ServerResponse.createByErrorMessage(mobile+"手机1分钟内发送短信不能超过1次");
        }

        String phoneCode = RandomDataUtil.generateSixRandomNum();

        boolean isSendSuccess = aliSmsTemplate.sendCodeSms(mobile, type,phoneCode);
        if (!isSendSuccess)
            return ServerResponse.createByErrorMessage("给手机"+mobile+"发送短信"+phoneCode+"失败");


        stringRedisTemplate.opsForValue().set(redisCodePrefix+mobile,phoneCode,Const.memberInfo.CODE_SESSION_EXTIME,TimeUnit.MINUTES);


        TMemberPhoneCode memberPhoneCode = assemMemberPhoneCode(phoneCode,mobile,type);

        int insertCount = memberPhoneCodeMapper.insert(memberPhoneCode);

        if (insertCount<=0){
            log.error("插入{}手机验证码{}相关信息失败",mobile,memberPhoneCode);
        }

        return ServerResponse.createBySuccess("发送短信成功",phoneCode);
    }

    public TMemberPhoneCode assemMemberPhoneCode(String phoneCode,String phone,String type){
        TMemberPhoneCode memberPhoneCode= new TMemberPhoneCode();
        memberPhoneCode.setPhone(phone);
        memberPhoneCode.setType(type);
        memberPhoneCode.setCode(phoneCode);
        return memberPhoneCode;
    }


    public boolean checkMobile(String loginAccount){
        TMemberExample memberExample = new TMemberExample();
        memberExample.createCriteria().andLoginacctEqualTo(loginAccount);
        long accoutNum = memberMapper.countByExample(memberExample);
        return accoutNum==0?true:false;
    }

    public boolean checkEmail(String email){
        TMemberExample memberExample = new TMemberExample();
        memberExample.createCriteria().andLoginacctEqualTo(email);
        long emailNum = memberMapper.countByExample(memberExample);
        return emailNum==0?true:false;
    }
}
