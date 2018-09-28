package com.shengxun.common.fileupload.impl;

import com.shengxun.common.fileupload.FileUploadService;
import com.shengxun.common.fileupload.oss.OssClientWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class OssFileUploadServiceImpl implements FileUploadService {

    private OssClientWrapper ossClientWrapper;

    private OssFileUploadServiceImpl(){}

    public OssFileUploadServiceImpl(Environment env){
        ossClientWrapper = OssClientWrapper.getInstance(env);
    }

    @Override
    public String uploadFile(File file) throws IOException {
        return ossClientWrapper.uploadFile(file);
    }

    @Override
    public String uploadFile(InputStream inputStream, String FileExtension) throws IOException {
        return ossClientWrapper.uploadFile(inputStream, FileExtension);
    }
}
