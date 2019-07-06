package com.atnanx.atcrowdfunding.project.controller;

import com.atnanx.atcrowdfunding.core.bean.TTag;
import com.atnanx.atcrowdfunding.core.bean.TType;
import com.atnanx.atcrowdfunding.core.common.ServerResponse;
import com.atnanx.atcrowdfunding.core.vo.resp.project.ProjectAllAllInfoVo;
import com.atnanx.atcrowdfunding.project.service.IProjectService;
import com.atnanx.atcrowdfunding.project.service.IReturnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "项目信息模块")
@RequestMapping("/project")
@RestController
public class ProjectInfoController {


    @Autowired
    IProjectService projectService;
    @Autowired
    IReturnService returnService;

    @ApiOperation("获取首页广告项目")
    @GetMapping("/adv")
    public ServerResponse<String> getIndexAdv() {
        return null;
    }

    @ApiOperation("获取项目总览列表")
    @GetMapping("/all/index")
    public ServerResponse getAllIndex() {
        return projectService.getAllProjectsInfos();
    }

    @ApiOperation("获取项目详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "项目id", required = true)
    })

    @GetMapping("/info/detail/{projectId}")
    public ServerResponse<ProjectAllAllInfoVo> getDetail(@PathVariable("projectId") Integer projectId){
        //因为设置了拦截器
        return projectService.getProjectAllDetail(projectId);
    }

    @ApiOperation("获取首页热门推荐")
    @GetMapping("/recommend/index")
    public ServerResponse<String> recommendIndex(){
        return null;
    }

    @ApiOperation("获取首页分类推荐")
    @GetMapping("/recommend/type")
    public ServerResponse<String> recommendType(){
        return null;
    }

    @ApiOperation("获取项目回报档位信息")
    @ApiImplicitParam(name = "returnId", value = "具体回报id", required = true)
    @GetMapping("/return/info")
    public ServerResponse returnInfo(Integer returnId){
        //因为设置了拦截器
        return returnService.getDetail(returnId);
    }



    @ApiOperation("获取项目系统标签信息")
    @GetMapping("/sys/tags")
    public ServerResponse<List<TTag>> sysTags(){


        ServerResponse<List<TTag>> serverResponse =  projectService.getSysTags();
        return serverResponse;
    }

    @ApiOperation("获取项目系统分类信息")
    @GetMapping("/sys/type")
    public ServerResponse<List<TType>> sysType(){

        ServerResponse<List<TType>> serverResponse =  projectService.getSysTypes();
        return serverResponse;
    }
}
