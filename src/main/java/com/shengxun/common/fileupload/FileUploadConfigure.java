package com.shengxun.common.fileupload;

import com.shengxun.common.exception.BizException;
import com.shengxun.common.fileupload.impl.OssFileUploadServiceImpl;
import com.shengxun.common.fileupload.oss.OssConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@Slf4j
public class FileUploadConfigure {

    @Bean
    public FileUploadService fileUploadService(Environment env) {
        String endpointRead = env.getProperty(OssConstants.OSS_ENDPOINT_READ);
        String endpointWrite = env.getProperty(OssConstants.OSS_ENDPOINT_WRITE);
        String accessKeyId = env.getProperty(OssConstants.OSS_ACCESS_KEY_ID);
        String accessKeySecret = env.getProperty(OssConstants.OSS_ACCESS_KEY_SECRET);
        String bucketName = env.getProperty(OssConstants.OSS_BUCKET_NAME);
        if (StringUtils.isBlank(endpointRead) || StringUtils.isBlank(endpointWrite) || StringUtils.isBlank(accessKeyId)
                || StringUtils.isBlank(accessKeySecret) || StringUtils.isBlank(bucketName)) {
            String message = "oss Missing configuration";
            throw new BizException("500", message);
        }
        return new OssFileUploadServiceImpl(env);
    }

}
