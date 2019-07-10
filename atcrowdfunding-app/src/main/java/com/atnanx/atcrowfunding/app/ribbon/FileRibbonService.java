package com.atnanx.atcrowfunding.app.ribbon;

import com.atnanx.atcrowdfunding.core.common.ServerResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
public class FileRibbonService {

    public ServerResponse<String> uploadPhoto(MultipartFile file,String accessToken){
      /*  ByteArrayResource[] byteArrayResources = new ByteArrayResource[file.length];

        for (int i = 0; i < file.length; i++) {
            try {
                byteArrayResources[i]  = new ByteArrayResource(file[i].getBytes());
            } catch (IOException e) {
                log.error("FileRibbonService报错 -->文件为空");
                e.printStackTrace();
            }
        }*/

        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<String, Object>();
        formData.add("file",file); // this is spring multipart file
        formData.add("accessToken",accessToken);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "multipart/form-data");
        headers.set("Accept", "text/plain");
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(formData, headers);
        RestTemplate restTemplate = getRestTemplate();
        ServerResponse<String>  serverResponse = restTemplate.postForObject("http://ATCROWDFUNDING-PROJECT/project/create/upload_photo", requestEntity, ServerResponse.class);
        return serverResponse;
    }

    public RestTemplate getRestTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper newObjectMapper = new ObjectMapper();
        newObjectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
        MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter=new MappingJackson2HttpMessageConverter();
        restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
        restTemplate.getMessageConverters().add(mappingJacksonHttpMessageConverter);
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        return restTemplate;
    }
}
