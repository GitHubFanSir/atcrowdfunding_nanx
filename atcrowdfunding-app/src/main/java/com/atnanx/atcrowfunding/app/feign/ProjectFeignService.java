package com.atnanx.atcrowfunding.app.feign;

import com.atnanx.atcrowdfunding.core.bean.TTag;
import com.atnanx.atcrowdfunding.core.bean.TType;
import com.atnanx.atcrowdfunding.core.common.ServerResponse;
import com.atnanx.atcrowdfunding.core.vo.req.project.BaseVo;
import com.atnanx.atcrowdfunding.core.vo.req.project.ProjectBaseInfoVo;
import com.atnanx.atcrowdfunding.core.vo.resp.project.ProjectAllAllInfoVo;
import com.atnanx.atcrowdfunding.core.vo.resp.project.ProjectAllInfoVo;
import com.atnanx.atcrowdfunding.core.vo.resp.project.ProjectReturnDetailVo;
import com.atnanx.atcrowfunding.app.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

//@RequestMapping("/project") fallback = FileUploadServiceHystrixImpl.class,
@FeignClient(value = "ATCROWDFUNDING-PROJECT", path = "/project",configuration = FeignConfig.class)
public interface ProjectFeignService {


    //第1步，初始化创建
    @PostMapping("/create/init")
    ServerResponse init(@RequestParam("accessToken") String accessToken,
                        @RequestParam(value = "hasReadProtocol",defaultValue = "1") String hasReadProtocol);

    //"获取项目系统标签信息"
    @GetMapping("/sys/tags")
    ServerResponse<List<TTag>> sysTags();

    //"获取项目系统分类信息"
    @GetMapping("/sys/type")
    ServerResponse<List<TType>> sysType();

    //第2步，保存众筹项目基本信息
    @PostMapping("/create/savebaseinfo")
    ServerResponse<String> saveBaseInfo(@RequestBody ProjectBaseInfoVo baseInfoVo,@RequestParam("accessToken") String accessToken);

    //第3步，保存众筹项目回报信息
    @PostMapping("/create/return")
    ServerResponse<String> addReturn(@RequestBody List<ProjectReturnDetailVo> returns);

    //第4步，保存众筹项目基本信息
    @PostMapping("/create/submit")
    ServerResponse<String> submit(@RequestBody BaseVo vo);

    //
    @GetMapping("/all/index")
    ServerResponse<List<ProjectAllInfoVo>> getAllIndex();

    //获取项目详细信息(封装的vo)
    @GetMapping("/info/detail/{projectId}")
    ServerResponse<ProjectAllAllInfoVo> getDetail(@PathVariable("projectId") Integer projectId);


    //图片上传 @RequestParam("file") @RequestBody @RequestPart(value = "file")
    @PostMapping(value = "/create/upload_photo" ,produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ServerResponse<String> uploadPhoto(@RequestPart(value = "file") MultipartFile file);

}
