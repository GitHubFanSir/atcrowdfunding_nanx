package com.atnanx.atcrowfunding.app.service.impl;

import com.atnanx.atcrowdfunding.core.common.ServerResponse;
import com.atnanx.atcrowfunding.app.component.OssTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class FileUploadServiceImpl {
    @Autowired
    private OssTemplate ossTemplate;

    public ServerResponse<String> uploadPhoto(MultipartFile multipartFile) {
        String uploadFileUrl = "";
        String originalFilename;
        byte[] fileBytes = {};
        String destFileName;

        if (multipartFile != null && !multipartFile.isEmpty()) {
            originalFilename = multipartFile.getOriginalFilename();
            try {
                fileBytes = multipartFile.getBytes();
            } catch (IOException e) {
                e.printStackTrace();
            }

            destFileName = UUID.randomUUID().toString().replace("-", "") + "_" + originalFilename;
            uploadFileUrl = ossTemplate.uploadFile(fileBytes, "pic", destFileName);


        }

        if (StringUtils.isBlank(uploadFileUrl)) {
            return ServerResponse.createByErrorMessage("未有文件上传或上传失败");
        }

        return ServerResponse.createBySuccess("上传文件成功", uploadFileUrl);
    }
}
