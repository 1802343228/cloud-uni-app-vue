package com.soft1851.files.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author crq
 */
public interface UploadService {
    /**
     * fdfs上传
     * @param file
     * @param fileExtName
     * @return
     * @throws Exception
     */
    String uploadFdfs(MultipartFile file,String fileExtName) throws Exception;
}
