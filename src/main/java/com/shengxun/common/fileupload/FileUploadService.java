package com.shengxun.common.fileupload;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface FileUploadService {
    /**
     * 上传文件
     *
     * @param file 文件
     * @return 返回文件路径
     */
    String uploadFile(File file) throws IOException;

    /**
     * 上传文件
     *
     * @param inputStream 输入流
     * @param fileName    文件名称 【可选】
     * @return 返回文件路径
     */
    String uploadFile(InputStream inputStream, String fileName) throws IOException;
}
