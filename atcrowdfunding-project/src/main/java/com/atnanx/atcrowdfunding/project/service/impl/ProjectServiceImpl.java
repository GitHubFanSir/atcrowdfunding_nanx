package com.atnanx.atcrowdfunding.project.service.impl;

import com.atnanx.atcrowdfunding.core.bean.*;
import com.atnanx.atcrowdfunding.core.common.ServerResponse;
import com.atnanx.atcrowdfunding.core.constant.Const;
import com.atnanx.atcrowdfunding.core.constant.state.ImgTypeEnum;
import com.atnanx.atcrowdfunding.core.constant.state.ProjectStatusEnum;
import com.atnanx.atcrowdfunding.core.util.DateTimeUtil;
import com.atnanx.atcrowdfunding.core.util.JsonUtil;
import com.atnanx.atcrowdfunding.core.vo.req.project.BaseVo;
import com.atnanx.atcrowdfunding.core.vo.req.project.ProjectBaseInfoVo;
import com.atnanx.atcrowdfunding.core.vo.req.project.ProjectReturnReqVo;
import com.atnanx.atcrowdfunding.core.vo.resp.project.ProjectAllAllInfoVo;
import com.atnanx.atcrowdfunding.core.vo.resp.project.ProjectAllInfoVo;
import com.atnanx.atcrowdfunding.project.component.OssTemplate;
import com.atnanx.atcrowdfunding.project.mapper.*;
import com.atnanx.atcrowdfunding.project.service.IProjectService;
import com.atnanx.atcrowdfunding.project.vo.ProjectRedisStorageVo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ProjectServiceImpl implements IProjectService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private OssTemplate ossTemplate;

    @Autowired
    private TTagMapper tagMapper;
    @Autowired
    private TTypeMapper typeMapper;

    @Autowired
    private TProjectMapper projectMapper;
    @Autowired
    private TReturnMapper returnMapper;
    @Autowired
    private TProjectImagesMapper projectImagesMapper;
    @Autowired
    private TProjectInitiatorMapper projectInitiatorMapper;

    @Autowired
    private TProjectTagMapper projectTagMapper;
    @Autowired
    private TProjectTypeMapper projectTypeMapper;

    @Override
    public ServerResponse initProject(String accessToken) {
        //ValueOperations
        String memberStr = stringRedisTemplate.opsForValue().get(Const.memberInfo.REDIS_LOGIN_MEMBER_PREFIX + accessToken);
        TMember member = JsonUtil.Json2Obj(memberStr, TMember.class);
        if (member == null) {
            return ServerResponse.createByErrorMessage("身份信息已失效，请重新登录");
        }

        //项目临时令牌，用于保存众筹项目相关信息
        String projectTempToken = UUID.randomUUID().toString().replace("-", "");
        ProjectRedisStorageVo projectRedisStorageVo = assemInitProjectRedisStorageVo(member.getId(), accessToken, projectTempToken);

        //ValueOperations
        stringRedisTemplate.opsForValue().set(Const.projectInfo.REDIS_TEMP_PROJECT_PREFIX + projectTempToken,
                JsonUtil.obj2Json(projectRedisStorageVo), Const.projectInfo.PROJECT_TEMP_SAVE_EXTIME, TimeUnit.MINUTES);

        Map map = Maps.newHashMap();
        map.put("projectTempToken", projectTempToken);
        return ServerResponse.createBySuccess("初始化项目成功", map);
    }

    public ProjectRedisStorageVo assemInitProjectRedisStorageVo(Integer memberId, String accessToken, String projectToken) {
        ProjectRedisStorageVo projectRedisStorageVo = new ProjectRedisStorageVo();
        projectRedisStorageVo.setProjectToken(projectToken);
        projectRedisStorageVo.setAccessToken(accessToken);
        projectRedisStorageVo.setMemberid(memberId);

        return projectRedisStorageVo;
    }

    @Override
    public ServerResponse<List<TType>> getSysTypes() {
        List<TType> typeList = typeMapper.selectByExample(null);
        if (CollectionUtils.isEmpty(typeList)) {
            return ServerResponse.createByErrorMessage("查询项目分类失败");
        }
        return ServerResponse.createBySuccess("查询项目分类成功", typeList);
    }

    @Override
    public ServerResponse<List<TTag>> getSysTags() {
        List<TTag> allTagList = tagMapper.selectByExample(null);
        if (CollectionUtils.isEmpty(allTagList)) {
            return ServerResponse.createByErrorMessage("查询项目标签失败");
        }

        TTagExample tagExample = new TTagExample();
        tagExample.createCriteria().andPidEqualTo(0);
        List<TTag> parentTagList = tagMapper.selectByExample(tagExample);


        List<TTag> allChildTagList = (List) CollectionUtils.subtract(allTagList, parentTagList);

        if (CollectionUtils.isEmpty(allChildTagList)) {
            return ServerResponse.createBySuccess("查询项目标签成功", allTagList);
        }

        List<TTag> parentChildTagList;
        for (TTag parentTag : parentTagList) {
            parentChildTagList = Lists.newArrayList();
            for (TTag childTag : allChildTagList) {
                if (childTag.getPid() == parentTag.getId()) {
                    parentChildTagList.add(childTag);
                }
            }

            parentTag.setChildTagList(parentChildTagList);
        }

        return ServerResponse.createBySuccess("查询项目标签成功", parentTagList);
    }

    @Override
    public ServerResponse<String> saveBaseInfo(ProjectBaseInfoVo baseInfoVo) {

        String tempProjectRedisKey = Const.projectInfo.REDIS_TEMP_PROJECT_PREFIX + baseInfoVo.getProjectToken();
        String originalAccessToken;
        String originalProjectToken;

        String tempProject = stringRedisTemplate.opsForValue().get(tempProjectRedisKey);

        ProjectRedisStorageVo projectRedisStorageVo = JsonUtil.Json2Obj(tempProject, ProjectRedisStorageVo.class);
        if (projectRedisStorageVo == null) {
            return ServerResponse.createByErrorMessage("临时创建的项目失效，请重新创建");
        } else {
            originalAccessToken = projectRedisStorageVo.getAccessToken();
            originalProjectToken = projectRedisStorageVo.getProjectToken();
        }

        BeanUtils.copyProperties(baseInfoVo, projectRedisStorageVo);
        projectRedisStorageVo.setAccessToken(originalAccessToken);
        projectRedisStorageVo.setProjectToken(originalProjectToken);


        //无法append追加，因为redis追加，会在后面形成新的对象json{},不好转化成对象，也不好取，
        //而且没办法他一开始就设计的这么大的对象，便于json转为一个大对象，也确实比较好
        //ValueOperations
        stringRedisTemplate.opsForValue().set(tempProjectRedisKey, JsonUtil.obj2Json(projectRedisStorageVo),
                Const.projectInfo.PROJECT_TEMP_SAVE_EXTIME, TimeUnit.MINUTES);

       /* stringRedisTemplate.opsForValue().append(tempProjectRedisKey,
                JsonUtil.obj2Json(baseInfoVo));
        stringRedisTemplate.expire(tempProjectRedisKey,Const.projectInfo.PROJECT_TEMP_SAVE_EXTIME,TimeUnit.MINUTES);*/

        return ServerResponse.createBySuccess("追加临时项目信息成功");
    }

    @Override
    public ServerResponse<List<String>> uploadPhoto(MultipartFile[] file) {
        List<String> uploadPhotoUrls = Lists.newArrayList();
        String originalFilename;
        byte[] fileBytes = {};
        String destFileName;
        for (MultipartFile multipartFile : file) {
            if (multipartFile != null && !multipartFile.isEmpty() && file.length > 0) {
                originalFilename = multipartFile.getOriginalFilename();
                try {
                    fileBytes = multipartFile.getBytes();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                destFileName = UUID.randomUUID().toString().replace("-", "") + "_" + originalFilename;
                String uploadFileUrl = ossTemplate.uploadFile(fileBytes, "pic", destFileName);
                uploadPhotoUrls.add(uploadFileUrl);
            }
        }

        if (CollectionUtils.isEmpty(uploadPhotoUrls)) {
            return ServerResponse.createByErrorMessage("未有文件上传或上传失败");
        }

        return ServerResponse.createBySuccess("上传文件成功", uploadPhotoUrls);
    }

    @Override
    public ServerResponse addReturn(List<ProjectReturnReqVo> returns) {
        if (CollectionUtils.isEmpty(returns)) {
            return ServerResponse.createByErrorMessage("请封装好数据再来");
        }
        String tempProjectRedisKey = Const.projectInfo.REDIS_TEMP_PROJECT_PREFIX + returns.get(0).getProjectToken();

        String tempProject = stringRedisTemplate.opsForValue().get(tempProjectRedisKey);

        ProjectRedisStorageVo projectRedisStorageVo = JsonUtil.Json2Obj(tempProject, ProjectRedisStorageVo.class);
        if (projectRedisStorageVo == null) {
            //在这里判断是为了避免redis数据有误，转化不成对象
            return ServerResponse.createByErrorMessage("临时创建的项目失效或数据出错，请重新创建");
        }

        //因为此时还没有把完整的项目 持久化到数据库，所以是无法得到projectId的
        List<TReturn> returnList = Lists.newArrayList();
        TReturn tReturn;
        for (ProjectReturnReqVo projectReturnReqVo : returns) {
            tReturn = new TReturn();
            BeanUtils.copyProperties(projectReturnReqVo, tReturn);
            returnList.add(tReturn);
        }

        if (CollectionUtils.isEmpty(returnList)) {
            return ServerResponse.createByErrorMessage("assem return数据错误");
        }

        projectRedisStorageVo.setProjectReturns(returnList);

        stringRedisTemplate.opsForValue().set(tempProjectRedisKey, JsonUtil.obj2Json(projectRedisStorageVo),
                Const.projectInfo.PROJECT_TEMP_SAVE_EXTIME, TimeUnit.MINUTES);


        return ServerResponse.createBySuccess("项目 装配return 回报数据成功");
    }

    //想让保存到数据库中后redis中还存在，但是后面是根据id 先从redis去，在从数据库取，redis没有，从数据库取出后放到redis缓存中
    //重要的是哪知道哪个token对哪个项目，只有id是清晰的，所以说临时项目无效，还是要以后自个儿取数据库放到redis中
    @Override
    @Transactional
    public ServerResponse submitOrDraftProjectToDb(BaseVo vo, Integer status) {

        String tempProjectToken = vo.getProjectToken();
        String tempProjectRedisKey = Const.projectInfo.REDIS_TEMP_PROJECT_PREFIX + tempProjectToken;

        String tempProject = stringRedisTemplate.opsForValue().get(tempProjectRedisKey);

        ProjectRedisStorageVo projectRedisStorageVo = JsonUtil.Json2Obj(tempProject, ProjectRedisStorageVo.class);
        if (projectRedisStorageVo == null) {
            //在这里判断是为了避免redis数据有误，转化不成对象
            return ServerResponse.createByErrorMessage("临时创建的项目失效或数据出错，请重新创建");
        }


        //1、保存项目信息
        TProject project = new TProject();
        BeanUtils.copyProperties(projectRedisStorageVo, project);

        project.setCreatedate(DateTimeUtil.dateToStr(new Date()));
        //0 草稿 1提交并审核
        project.setStatus(String.valueOf(status));
        int insertCount = projectMapper.insertSelective(project);
        if (insertCount <= 0) {
            log.error("众筹项目{}创建project插入失败", projectRedisStorageVo.getProjectToken());
            return ServerResponse.createByErrorMessage("众筹项目 project信息插入失败");
        }

        //2 、保存头图与项目详情图片信息
        String headerImage = projectRedisStorageVo.getHeaderImage();
        TProjectImages projectImages = new TProjectImages(null, project.getId(), headerImage, ImgTypeEnum.HEADER_IMG.getCode());
        int headerImageInsertCount = projectImagesMapper.insertSelective(projectImages);
        if (headerImageInsertCount <= 0) {
            log.error("头图插入失败", projectImages);
            return ServerResponse.createByErrorMessage("头图信息插入失败");
        }

        List<String> detailsImageList = projectRedisStorageVo.getDetailsImage();
        detailsImageList.forEach(detailsImage -> {
            TProjectImages detailsProjectImage = new TProjectImages(null, project.getId(), detailsImage, ImgTypeEnum.DETAIL_IMG.getCode());
            int detailsImageInsertCount = projectImagesMapper.insertSelective(detailsProjectImage);
            if (detailsImageInsertCount <= 0) {
                log.error("详情图片插入失败", detailsImage);
            }
        });


        //保存项目发起人表
        TProjectInitiator projectInitiator = new TProjectInitiator();
        BeanUtils.copyProperties(projectRedisStorageVo, projectInitiator);
        int projectInitiatorInsertCount = projectInitiatorMapper.insertSelective(projectInitiator);
        if (projectInitiatorInsertCount <= 0) {
            log.error("项目{},项目名:{}发起人表插入失败", project.getId(),project.getName());
        }

        //3、保存项目标签
        List<Integer> tagids = projectRedisStorageVo.getTagids();
        tagids.forEach((tagid)->{
            TProjectTag tag = new TProjectTag();
            tag.setProjectid(project.getId());
            tag.setTagid(tagid);
            int i = projectTagMapper.insertSelective(tag);
            if (i <= 0) {
                log.error("项目{}标签{}插入失败",project.getId(),tagid);
            }

        });

        //4、保存项目分类
        List<Integer> typeids = projectRedisStorageVo.getTypeids();
        typeids.forEach((typeid)->{
            TProjectType type = new TProjectType();
            type.setProjectid(project.getId());
            type.setTypeid(typeid);
            int i = projectTypeMapper.insertSelective(type);
            if (i <= 0) {
                log.error("项目{}分类{}插入失败",project.getId(),typeid);
            }
        });


        //5、保存项目的回报档位信息
        List<TReturn> projectReturnList = projectRedisStorageVo.getProjectReturns();
        projectReturnList.forEach((projectReturn)->{
            projectReturn.setProjectid(project.getId());
            int insertCount2 = returnMapper.insertSelective(projectReturn);
            if (insertCount2 < 0) {
                log.error("treturn {}插入失败", projectReturn.getContent());
            }
        });

        //6、项目保存成功，将redis中之前的数据删除
        stringRedisTemplate.delete(Const.projectInfo.REDIS_TEMP_PROJECT_PREFIX+vo.getProjectToken());

        return ServerResponse.createBySuccessMessage("提交项目，进行"+ProjectStatusEnum.getProjectStatus(String.valueOf(status))+"成功");
    }

    @Override
    public ServerResponse<ProjectAllAllInfoVo> getProjectAllDetail(Integer projectId) {

        ProjectAllAllInfoVo allInfoVo = new ProjectAllAllInfoVo();
        //1、查询项目的基本信息
        TProject project = projectMapper.selectByPrimaryKey(projectId);
        BeanUtils.copyProperties(project,allInfoVo);

        //2、查出头图地址
        TProjectImages projectImages = this.getProjectHeaderImage(projectId);
        allInfoVo.setHeaderImage(projectImages.getImgurl());
        //3、查出项目的详情图
        List<TProjectImages> detailImages =  this.getProjectDetailImage(projectId);
        List<String> images = new ArrayList<>();
        for (TProjectImages detailImage : detailImages) {
            images.add(detailImage.getImgurl());
        }
        allInfoVo.setDetailImages(images);

        //4、查询项目的所有档位信息
        List<TReturn> returns = this.getProjectAllReturns(projectId);
        allInfoVo.setReturns(returns);

        return ServerResponse.createBySuccess("获取项目详情成功",allInfoVo);
    }

    @Override
    public ServerResponse getAllProjectsInfos() {
        List<ProjectAllInfoVo> allProjectsInfos = projectMapper.getAllProjectsInfos();
        if (CollectionUtils.isEmpty(allProjectsInfos)){
            return ServerResponse.createByErrorMessage("还没有项目呢");
        }
        return ServerResponse.createBySuccess("查找所有项目成功",allProjectsInfos);
    }

    public TProjectImages getProjectHeaderImage(Integer projectId) {
        TProjectImagesExample example = new TProjectImagesExample();
        example.createCriteria().andProjectidEqualTo(projectId).andImgtypeEqualTo((byte)0);
        List<TProjectImages> tProjectImages = projectImagesMapper.selectByExample(example);
        return tProjectImages==null?null:tProjectImages.get(0);
    }

    public List<TProjectImages> getProjectDetailImage(Integer projectId) {
        TProjectImagesExample example = new TProjectImagesExample();
        example.createCriteria().andProjectidEqualTo(projectId).andImgtypeEqualTo((byte)1);
        List<TProjectImages> tProjectImages = projectImagesMapper.selectByExample(example);
        return tProjectImages;
    }

    public List<TReturn> getProjectAllReturns(Integer projectId) {
        TReturnExample example = new TReturnExample();
        example.createCriteria().andProjectidEqualTo(projectId);
        List<TReturn> returns = returnMapper.selectByExample(example);
        return returns;
    }
}
