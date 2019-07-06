package com.atnanx.atcrowdfunding.project.controller;

import com.atnanx.atcrowdfunding.core.common.ServerResponse;
import com.atnanx.atcrowdfunding.core.vo.req.project.BaseVo;
import com.atnanx.atcrowdfunding.core.vo.req.project.ProjectBaseInfoVo;
import com.atnanx.atcrowdfunding.core.vo.req.project.ProjectReturnReqVo;
import com.atnanx.atcrowdfunding.project.component.OssTemplate;
import com.atnanx.atcrowdfunding.project.service.IProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(tags = "项目发起模块")
@RequestMapping("/project/create")
@RestController
@Slf4j
public class ProjectCreateController {

    @Autowired
    IProjectService projectService;

    @Autowired
    OssTemplate ossTemplate;

    @Autowired
    StringRedisTemplate redisTemplate;
    
    @ApiOperation("确认项目法人信息")
    @ApiImplicitParams({
            @ApiImplicitParam(),
    })
    @PostMapping("/confirm/legal")
    public ServerResponse<String> confirmLegal(){
        return null;
    }



    @ApiOperation("项目创建第1步-项目初始创建")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accessToken", value = "访问令牌", required = true),
            @ApiImplicitParam(name = "hasReadProtocol", value = "是否阅读过协议 0 阅读过 1没阅读过", required = true)
    })
    @PostMapping("/init")
    public ServerResponse init(@RequestParam("accessToken") String accessToken,@RequestParam(value = "hasReadProtocol",defaultValue = "1") String hasReadProtocol ){
        //点击阅读并同意协议，新创一个临时项目；生成一个项目的临时Token；
        //<ProjectTempVo>
        if (StringUtils.isBlank(accessToken)){
            return ServerResponse.createByErrorMessage("没有携带访问令牌不能访问");
        }
        if (StringUtils.equals("1",hasReadProtocol)){
            return ServerResponse.createByErrorMessage("还没有阅读过协议");
        }

        return projectService.initProject(accessToken);
    }


    @ApiOperation("项目创建第2步-项目基本信息保存")
    @PostMapping("/savebaseinfo")
    public ServerResponse<String> saveBaseInfo(ProjectBaseInfoVo baseInfoVo){
        return projectService.saveBaseInfo(baseInfoVo);
    }


    /**
     * List<TReturn> SpringMVC默认没办法接受；
     *
     *  <input name="returns[0].supportmoney" value="'/>
     *  <input name="returns[0].content" value="'/>
     *  <input name="returns[0].freight" value="'/>
     *  <input name="returns[1].supportmoney"/>
     *  <input name="returns[2].supportmoney"/>
     *  <input name="returns[3]"/>
     *  <input name="returns[4]"/>
     *
     *  //SpringMVC；
     *  1、可以返回的对象写成json字符串
     *  2、可以将提交的json字符串逆转为对象；放在请求体中
     *      [{id:1},{id:2}]
     *  3、提交数据的时候，必须Post提交，提交的数据必须json；
     *        // $.ajax("return",json)
     *        $.post("return",json)；默认使用 application/x-www-form-urlencoded;k=v&k=v；
     *        $.ajax({
     *            url:"return",
     *            data:jsonStr,//k=v&k=v
     *            contentType:false,
     *            processData:false,
     *            success:function(data){
     *
     *            },
     *            error:function(){
     *
     *            }
     *        })
     *
     *
     * SpringMVC方法签名；@RequestBody List<TReturn> returns；
     *  请求体默认就应该是json字符串。
     * @param
     * @return
     */
    @ApiOperation("项目创建第3步-添加项目回报档位")
    @ApiImplicitParam(name = "accessToken", value = "访问令牌", required = true)
    @PostMapping("/return")
    public ServerResponse<String> addReturn(@RequestBody List<ProjectReturnReqVo> returns,String accessToken){
        ServerResponse serverResponse = projectService.addReturn(returns);
        return serverResponse;
    }

    @ApiOperation("删除项目回报档位")
    @DeleteMapping("/return")
    public ServerResponse<String> deleteReturn(){
        return null;
    }


    @ApiOperation("项目提交审核申请")
    @ApiImplicitParam(name = "status", value = "保存状态，1保存项目，0保存草稿 管理不会审核", required = true)
    @PostMapping("/submit")
    public ServerResponse<String> submit(BaseVo vo,Integer status){
        ServerResponse serverResponse = projectService.submitOrDraftProjectToDb(vo,status);
        return serverResponse;
    }

    @ApiOperation("项目草稿保存")
    @ApiImplicitParam(name = "status", value = "保存状态，1 保存项目，0 保存草稿 管理不会审核", required = true)
    @PostMapping("/tempsave")
    public ServerResponse<String> tempsave(BaseVo vo,Integer status){
        ServerResponse serverResponse = projectService.submitOrDraftProjectToDb(vo,status);
        return serverResponse;
    }


    /**
     * 浏览器想要文件上传的几个要素；
     *    <form method="post" enctype="multipart/form-data">
     *         <input type="file" name="file" multiple>
     *
     *             <input type="file" name="header" >
     *             <input type="file" name="header" >
     *     </form>
     * 服务器：
     *
     * @param file
     * @return
     */
    @ApiOperation("项目图片上传")
    @PostMapping("/upload_photo")
    public ServerResponse<List<String>> uploadPhoto(@RequestParam("file") MultipartFile[] file,
                                            @RequestParam("accessToken") String accessToken) {

        return projectService.uploadPhoto(file);
    }
}
