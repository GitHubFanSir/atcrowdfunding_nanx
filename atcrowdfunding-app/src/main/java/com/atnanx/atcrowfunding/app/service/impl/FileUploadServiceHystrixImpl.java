package com.atnanx.atcrowfunding.app.service.impl;

import com.atnanx.atcrowdfunding.core.bean.TTag;
import com.atnanx.atcrowdfunding.core.bean.TType;
import com.atnanx.atcrowdfunding.core.common.ServerResponse;
import com.atnanx.atcrowdfunding.core.vo.req.project.BaseVo;
import com.atnanx.atcrowdfunding.core.vo.req.project.ProjectBaseInfoVo;
import com.atnanx.atcrowdfunding.core.vo.resp.project.ProjectAllAllInfoVo;
import com.atnanx.atcrowdfunding.core.vo.resp.project.ProjectAllInfoVo;
import com.atnanx.atcrowdfunding.core.vo.resp.project.ProjectReturnDetailVo;
import com.atnanx.atcrowfunding.app.feign.ProjectFeignService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

//@Component
public class FileUploadServiceHystrixImpl implements ProjectFeignService {

    @Override
    public ServerResponse init(String accessToken, String hasReadProtocol) {
        return ServerResponse.createByErrorMessage("众筹项目第一步初始化 error 熔断");
    }

    @Override
    public ServerResponse<List<TTag>> sysTags() {
        return ServerResponse.createByErrorMessage("得到众筹项目标签 error 熔断");
    }

    @Override
    public ServerResponse<List<TType>> sysType() {
        return ServerResponse.createByErrorMessage("得到众筹项目分类 error 熔断");
    }

    @Override
    public ServerResponse<String> saveBaseInfo(ProjectBaseInfoVo baseInfoVo,String accessToken) {
        return ServerResponse.createByErrorMessage("众筹项目第二部保存基本信息 error 熔断");
    }

    @Override
    public ServerResponse<String> addReturn(List<ProjectReturnDetailVo> returns) {
        return ServerResponse.createByErrorMessage("众筹项目第三部添加回报 error 熔断");
    }

    @Override
    public ServerResponse<String> submit(BaseVo vo) {
        return ServerResponse.createByErrorMessage("众筹项目submit error 熔断");
    }

    @Override
    public ServerResponse<List<ProjectAllInfoVo>> getAllIndex() {
        return ServerResponse.createByErrorMessage("getAllIndex error 熔断");
    }

    @Override
    public ServerResponse<ProjectAllAllInfoVo> getDetail(Integer projectId) {
        return ServerResponse.createByErrorMessage("getDetail error 熔断");
    }

    @Override
    public ServerResponse<String> uploadPhoto(MultipartFile file) {
        return null;
    }


   /* @Override
    public ServerResponse<List<String>> uploadPhoto(MultipartFile[] file, String accessToken) {
        return ServerResponse.createByErrorMessage("upload error 熔断");
    }*/
}
