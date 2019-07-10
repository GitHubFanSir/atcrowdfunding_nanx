package com.atnanx.atcrowdfunding.project.service;

import com.atnanx.atcrowdfunding.core.bean.TTag;
import com.atnanx.atcrowdfunding.core.bean.TType;
import com.atnanx.atcrowdfunding.core.common.ServerResponse;
import com.atnanx.atcrowdfunding.core.vo.req.project.BaseVo;
import com.atnanx.atcrowdfunding.core.vo.req.project.ProjectBaseInfoVo;
import com.atnanx.atcrowdfunding.core.vo.req.project.ProjectReturnReqVo;
import com.atnanx.atcrowdfunding.core.vo.resp.project.ProjectAllAllInfoVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProjectService {
    /**
     * 众筹项目创建并初始化
     * @return
     */
    ServerResponse initProject(String accessToken);




    ServerResponse<List<TType>> getSysTypes();

    ServerResponse<List<TTag>> getSysTags();

    ServerResponse<String> saveBaseInfo(ProjectBaseInfoVo baseInfoVo);

    ServerResponse<String> uploadPhoto(MultipartFile file);

    ServerResponse addReturn(List<ProjectReturnReqVo> returns);

    ServerResponse submitOrDraftProjectToDb(BaseVo vo, Integer status);

    ServerResponse<ProjectAllAllInfoVo> getProjectAllDetail(Integer projectId);

    ServerResponse getAllProjectsInfos();
}
