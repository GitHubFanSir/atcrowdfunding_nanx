package com.atnanx.atcrowdfunding.project.component;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectResult;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 发送短信Template
 */

@ConfigurationProperties(prefix = "aliyun.oss")
@Component
@Data
@Slf4j
public class OssTemplate {
    // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
    private String accessKeyId;
    private String accessKeySecret;
    // Endpoint地域节点 以杭州为例，其它Region请按实际情况填写。
    private String endpoint;

    private String bucketName;
    /**
     * 创建桶容器(存储空间)
     */
    public void createOssBucket() {

        // 创建OSSClient实例。这几个变量是已有的桶实例，去写成自己要创建的桶名字：
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        // 创建存储空间。
        /*CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
            // 设置存储空间的权限为公共读，默认是私有。
        createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
            // 设置存储空间的存储类型为低频访问类型，默认是标准类型。
        createBucketRequest.setStorageClass(StorageClass.IA);

        // 设置存储空间的访问权限为私有。有ossClient直接设置即可
        ossClient.setBucketAcl("<yourBucketName>", CannedAccessControlList.Private);
        // 获取存储空间的访问权限。
        AccessControlList acl = ossClient.getBucketAcl("<yourBucketName>");
        */
        ossClient.createBucket(bucketName);
        // 关闭OSSClient。
        ossClient.shutdown();
    }
    /**
     * 列举存储空间
     * 存储空间按照字母顺序
     */

    /**
     * 上传文件至OSS
     * @param sourceFile
     * @param targetDir
     * @param destFileName
     */
    public String uploadFile(byte[] sourceFile,String targetDir,String destFileName){
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

        // 上传内容到指定的存储空间（bucketName）并保存为指定的文件名称（objectName）。
        //String content = "Hello OSS"; 文件怎么用string编码显示，会整个报错的，所以方法设置时就传字节


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formatDate = dateFormat.format(new Date());
        //拼接指定的路径
        PutObjectResult putObjectResult = ossClient.putObject(bucketName, targetDir + "/" + formatDate + "/" + destFileName, new ByteArrayInputStream(sourceFile));

        log.debug(putObjectResult.getETag());
        // 设置URL过期时间为1小时。
            //      Date expiration = new Date(new Date().getTime() + 3600 * 1000);
        //        URL url = ossClient.generatePresignedUrl(bucketName, targetDir + "/" + formatDate + "/" + destFileName, expiration);
        // 关闭OSSClient。
        ossClient.shutdown();

        //返回这个东西的访问地址；
        //oss-cn-shanghai.aliyuncs.com
        //https://atcrowdfunding-hyj2.oss-cn-shanghai.aliyuncs.com/pic/2019-06-23/09cf9ad6569a4e65ae4c6d37579e1bc5_QQ%E6%B5%8F%E8%A7%88%E5%99%A8%E6%88%AA%E5%9B%BE20190228171443.png
        String url = "https://"+bucketName+"."+endpoint+"/"+targetDir+"/"+formatDate+"/"+destFileName;
        return url;
    }

    /**
     *  上传文件流。
     * @param file
     * @param sourceFileName
     * @param destFileName
     */
    public void uploadFile(File file,String sourceFileName,String destFileName) throws FileNotFoundException{
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

        // 上传内容到指定的存储空间（bucketName）并保存为指定的文件名称（objectName）。
        //String content = "Hello OSS"; 文件怎么用string编码显示，会整个报错的，所以方法设置时就传字节

        // 上传文件流。
        InputStream inputStream = new FileInputStream(file);
        ossClient.putObject(bucketName, destFileName+"_"+sourceFileName, inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();
    }


    /**
     * 1次性下载显示文件内容
     * 下载文件，不能是图片
     * 用于获取文件的文本内容
     * 这是每次遍历读取一行输出1行，只能用来1次性显示，不能永久获取
     */
    public void downloadFile(String sourceFileName) throws IOException {

        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

        // 调用ossClient.getObject返回一个OSSObject实例，该实例包含文件内容及文件元信息。
        OSSObject ossObject = ossClient.getObject(bucketName, sourceFileName);
        // 调用ossObject.getObjectContent获取文件输入流，可读取此输入流获取其内容。
        InputStream content = ossObject.getObjectContent();
        if (content != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
            while (true) {
                String line = reader.readLine();
                if (line == null) break;
                System.out.println("\n" + line);
            }
            // 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
            content.close();
        }
        // 关闭OSSClient。
        ossClient.shutdown();
    }

    /**
     * 删除文件
     */
    public void deleteFile(String deleteFileName){
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        // 删除文件。
        ossClient.deleteObject(bucketName, deleteFileName);
        // 关闭OSSClient。
        ossClient.shutdown();
    }

    /*
    * // 创建ClientConfiguration实例，按照您的需要修改默认参数。
        ClientConfiguration conf = new ClientConfiguration();
        // 开启支持CNAME。CNAME是指将自定义域名绑定到存储空间上。
        conf.setSupportCname(true);
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret, conf);
    */
}

