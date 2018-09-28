package com.shengxun.common.fileupload.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.shengxun.common.util.StringGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Objects;

@Slf4j
public class OssClientWrapper {

    private Environment environment;
    private static OssClientWrapper ossClientWrapper = null;

    private OssClientWrapper() {
    }

    private OssClientWrapper(Environment env) {
        this.environment = env;
    }

    public static OssClientWrapper getInstance(Environment env) {
        if (Objects.isNull(ossClientWrapper)) {
            synchronized (OssClientWrapper.class) {
                ossClientWrapper = new OssClientWrapper(env);
            }
        }
        return ossClientWrapper;
    }

    private OSSClient getOssWriteClient(Environment env) {
        // 创建OSSClient实例
        String endpoint = env.getProperty(OssConstants.OSS_ENDPOINT_WRITE);
        String accessKeyId = env.getProperty(OssConstants.OSS_ACCESS_KEY_ID);
        String accessKeySecret = env.getProperty(OssConstants.OSS_ACCESS_KEY_SECRET);
        return new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }

    private OSSClient getOssReadClient(Environment env) {
        // 创建OSSClient实例
        String endpoint = env.getProperty(OssConstants.OSS_ENDPOINT_READ);
        String accessKeyId = env.getProperty(OssConstants.OSS_ACCESS_KEY_ID);
        String accessKeySecret = env.getProperty(OssConstants.OSS_ACCESS_KEY_SECRET);
        return new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }

    public String uploadFile(File file) throws IOException {
        return this.uploadFile(FileUtils.openInputStream(file), file.getName());
    }

    public String uploadFile(InputStream inputStream, String fileName) {
        OSSClient ossWriteClient = getOssWriteClient(environment);
        // 文件
        String bucketName = environment.getProperty(OssConstants.OSS_BUCKET_NAME);
        String fileDir = createFolder(ossWriteClient, bucketName, fileName);
        String objectKey = getNewFileName(fileDir, fileName);
        // 上传
        ossWriteClient.putObject(bucketName, objectKey, inputStream);
        // 关闭client
        ossWriteClient.shutdown();
        Date expiration = new Date(new Date().getTime() + (1000L * 60 * 60 * 24 * 365 * 100));
        OSSClient ossReadClient = getOssReadClient(environment);
        String url = ossReadClient.generatePresignedUrl(bucketName, objectKey, expiration).toString();
        ossReadClient.shutdown();
        return url;
    }

    private String getNewFileName(String fileDir, String fileName) {
        String extension = FilenameUtils.getExtension(fileName);
        String newName = StringGenerator.get32UUID() + (StringUtils.isNotBlank(extension) ? "." + extension : "");
        return fileDir.concat(newName);
    }

    private String createFolder(OSSClient ossClient, String bucketName, String fileName) {
        String fileDir = "";
        if (StringUtils.isBlank(fileName)) {
            return fileDir;
        }
        if (fileName.contains(OssConstants.SEPARATOR)) {
            fileDir = fileName.substring(0, fileName.lastIndexOf(OssConstants.SEPARATOR)) + OssConstants.SEPARATOR;
            // 判断文件夹是否存在，不存在则创建
            if (!ossClient.doesObjectExist(bucketName, fileDir)) {
                // 创建文件夹
                ossClient.putObject(bucketName, fileDir, new ByteArrayInputStream(new byte[0]));
                log.info("创建文件夹成功");
                // 得到文件夹名
                OSSObject object = ossClient.getObject(bucketName, fileDir);
                fileDir = object.getKey();
            }
        }
        return fileDir;
    }
}
