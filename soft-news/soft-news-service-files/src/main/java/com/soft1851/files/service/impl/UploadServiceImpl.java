package com.soft1851.files.service.impl;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.soft1851.files.service.UploadService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.webresources.FileResource;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * @author crq
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UploadServiceImpl implements UploadService {
    public final FastFileStorageClient fastFileStorageClient;

    public final FileResource fileResource;
    public final Sid sid;

    @Override
    public String uploadFdfs(MultipartFile file, String fileExtName) throws Exception {
        InputStream inputStream = file.getInputStream();
        StorePath storePath = fastFileStorageClient.uploadFile(inputStream,
                file.getSize(),fileExtName,null
                );
        inputStream.close();
        return storePath.getFullPath();
    }
}
