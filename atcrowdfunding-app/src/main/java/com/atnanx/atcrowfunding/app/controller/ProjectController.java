package com.atnanx.atcrowfunding.app.controller;

import com.atnanx.atcrowdfunding.core.bean.TTag;
import com.atnanx.atcrowdfunding.core.bean.TType;
import com.atnanx.atcrowdfunding.core.common.ServerResponse;
import com.atnanx.atcrowdfunding.core.vo.req.project.BaseVo;
import com.atnanx.atcrowdfunding.core.vo.req.project.ProjectBaseInfoVo;
import com.atnanx.atcrowdfunding.core.vo.resp.member.MemberLoginRespVo;
import com.atnanx.atcrowdfunding.core.vo.resp.project.ProjectAllAllInfoVo;
import com.atnanx.atcrowdfunding.core.vo.resp.project.ProjectReturnDetailVo;
import com.atnanx.atcrowfunding.app.feign.ProjectFeignService;
import com.atnanx.atcrowfunding.app.service.impl.FileUploadServiceImpl;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    ProjectFeignService projectFeignService;
    /*@Autowired
    private FileFeignService fileFeignService;*/
    @Autowired
    private FileUploadServiceImpl fileUploadService;

    /**
     * 计算两天间隔了多少天
     * @param preTime
     * @param lastTime
     * @return
     */
    private  int differentDayMillisecond (Date preTime,Date lastTime)
    {
        int day = (int)((lastTime.getTime()-preTime.getTime())/(3600*24*1000));//单位ms,1 day
        return day;
    }


    @GetMapping("/info/{id}")
    public String projectInfo(@PathVariable("id") Integer id,Model model) throws ParseException {
        //1、调用atcrowdfunding-project查询项目的详细信息
        ServerResponse<ProjectAllAllInfoVo> response = projectFeignService.getDetail(id);
        if(response.isSuccess()){
            ProjectAllAllInfoVo data = response.getData();

            String createdate = data.getCreatedate();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parse = format.parse(createdate);
            Integer day = data.getDay();
            Date current = new Date();
            int i = differentDayMillisecond(parse, current);
            //20
            data.setTimeLife(day-i);

            model.addAttribute("projectDetail",data);
        }
        return "project/project";
    }



    /**
     * 第0步：发起众筹跳到，阅读并同意协议
     * @return
     */
    @GetMapping("/start.html")
    public String startProject(){


        return "project/start";
    }

    /**
     * 第1步：阅读并同意协议跳到项目基本信息录入页面
     * 跳转页面，并需要传参做验证
     */
    @GetMapping("/start-step-1.html")
    public String startstep1(HttpSession session, Model model){
        //展示step1页面之前
        //1、获取项目令牌;
        MemberLoginRespVo loginUser = (MemberLoginRespVo) session.getAttribute("loginUser");
        ServerResponse response = projectFeignService.init(loginUser.getAccessToken(),"0");
        String projectTempToken = (String) response.getData();
        session.setAttribute("projectToken",projectTempToken);

        //2、远程查询分类信息
        ServerResponse<List<TType>> listServerResponse = projectFeignService.sysType();
        //3、远程查询标签信息
        ServerResponse<List<TTag>> listServerResponse1 = projectFeignService.sysTags();

        List<TTag> alltags = listServerResponse1.getData();
        //整理父子关系
        List<TTag> tags = new ArrayList<>();
        for (TTag tTag : alltags) {
            //遍历所有标签取出父标签
            if(tTag.getPid() == 0){
                tags.add(tTag);
            }
        }

        for (TTag tag : tags) {
            //为每一个父标签找子标签
            for (TTag tTag : alltags) {
                if(tTag.getPid() == tag.getId()){
                    tag.getChildTagList().add(tTag);
                }
            }
        }

        model.addAttribute("sysTypes",listServerResponse.getData());
        model.addAttribute("sysTags",tags);
        return "/project/start-step-1";
    }

    /**
     * 第2步：项目基本信息录入页面点击下一步，来到回报设置页
     */
    @PostMapping("/start-step-2.html")
    public String startstep2(ProjectBaseInfoVo projectBaseInfoVo,
                             @RequestParam("header") MultipartFile file,
                             @RequestParam("details") MultipartFile[] files,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) throws IOException {
        //收集页面的数据
        log.debug("收集到的页面的数据...{}",projectBaseInfoVo);
        MemberLoginRespVo loginUser = (MemberLoginRespVo) session.getAttribute("loginUser");

        //上传头图
        if(!file.isEmpty()){
            log.debug("头图{}文件上传完成",file.getOriginalFilename());
           /* FileRibbonService fileRibbonService = new FileRibbonService();
            ServerResponse<String> response = fileRibbonService.uploadPhoto(file, loginUser.getAccessToken());*/
            ServerResponse<String> response = fileUploadService.uploadPhoto(file);
            if (response.isSuccess()){
                String url = response.getData();
                projectBaseInfoVo.setHeaderImage(url);
                log.info(response.getMsg());
            } else {
                log.error("头图上传失败");
                log.error(response.getMsg());
            }
//            String url = ossTemplate.upload(file.getBytes(), file.getOriginalFilename());

        }

        List<String> details=Lists.newArrayList();
        //上传详情图
        if(files!=null){
//            FileRibbonService fileRibbonService = new FileRibbonService();

            if (files.length > 0){
                for (MultipartFile multipartFile : files) {
                    if (multipartFile != null && !multipartFile.isEmpty()) {
                        log.debug("详情{}文件上传完成",multipartFile.getOriginalFilename());
                        ServerResponse<String> response = fileUploadService.uploadPhoto(multipartFile);
//                        ServerResponse<String> response = fileRibbonService.uploadPhoto(multipartFile, loginUser.getAccessToken());
                        String url = response.getData();
                        details.add(url);
                    }
                }
            }
        }
        projectBaseInfoVo.setDetailsImage(details);

        //把这两个令牌封装好，这些之前都在session中
        projectBaseInfoVo.setAccessToken(loginUser.getAccessToken());
        projectBaseInfoVo.setProjectToken((String) session.getAttribute("projectToken"));


        log.debug("准备好的项目所有信息的vo数据：{}，这些数据将发送出去",projectBaseInfoVo);
        //远程服务保存项目信息
        ServerResponse<String> response = projectFeignService.saveBaseInfo(projectBaseInfoVo,loginUser.getAccessToken());
        if(response.isSuccess()){
            return "redirect:/start-step-2.html";
        }else {
            //失败回到之前页面并进行回显
            redirectAttributes.addFlashAttribute("vo",projectBaseInfoVo);
            String msg = response.getMsg();
            log.error(msg);
            redirectAttributes.addFlashAttribute("msg",msg);
            return "redirect:/project/start-step-1.html";
        }


    }

    /**
     * 第3步：回报设置页点击下一步，来到发起人信息录入页
     */
    @ResponseBody
    @PostMapping("/start-step-3.html")
    public ServerResponse<String> startstep3(@RequestBody List<ProjectReturnDetailVo> returns, HttpSession session){
//
        MemberLoginRespVo loginUser = (MemberLoginRespVo) session.getAttribute("loginUser");
//        @ApiModelProperty(value = "访问令牌",required = true)
//        private String accessToken;//访问令牌
//        @ApiModelProperty(value = "项目临时令牌",required = true)
//        private String projectToken;//项目的临时token；项目的唯一标识
        for (ProjectReturnDetailVo returnVo : returns) {
            returnVo.setAccessToken(loginUser.getAccessToken());
            returnVo.setProjectToken((String) session.getAttribute("projectToken"));
        }

        log.debug("收到的页面提交来的数据：{}",returns);
        ServerResponse<String> ServerResponse = projectFeignService.addReturn(returns);
        return ServerResponse;
    }

    /**
     * 第4步：发起人信息录入页点击提交或者保存草稿，来到成功提示页
     */
    @GetMapping("/start-step-4.html")
    public String startstep4(HttpSession session){
        MemberLoginRespVo loginUser = (MemberLoginRespVo) session.getAttribute("loginUser");
        String projectToken = (String) session.getAttribute("projectToken");

        BaseVo baseVo = new BaseVo();
        baseVo.setAccessToken(loginUser.getAccessToken());
        baseVo.setProjectToken(projectToken);
        ServerResponse<String> submit = projectFeignService.submit(baseVo);
        if(submit.isSuccess()){
            //项目保存成功了
            return "/protected/project/start-step-4";
        }else {
            return "redirect:/start-step-3.html";
        }

    }

}
