package com.soft1851.files.controller;

import com.soft1851.api.controller.files.FileUploadControllerApi;
import com.soft1851.files.resource.FileResource;
import com.soft1851.files.service.UploadService;
import com.soft1851.result.GraceResult;
import com.soft1851.result.ResponseStatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * @author crq
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FileUploadController implements FileUploadControllerApi {
    private final UploadService uploadService;
    private final FileResource fileResource;

    @Override
    public GraceResult uploadFace(String userId, MultipartFile file) throws Exception {
        String path;
        if(file != null) {
            String fileName = file.getOriginalFilename();
            if(StringUtils.isNoneBlank(fileName)){
                String[] fileNameArr = fileName.split("\\.");
                String suffix = fileNameArr[fileNameArr.length - 1];
                if(!"png".equalsIgnoreCase(suffix) &&
                !"jpg".equalsIgnoreCase(suffix) &&
                        !"jpeg".equalsIgnoreCase(suffix)
                ){
                    return GraceResult.errorCustom(ResponseStatusEnum.FILE_FORMATTER_FAILD);
                }
                //执行上传服务 得到回调路径
                path = uploadService.uploadFdfs(file,suffix);
            } else {
                return GraceResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
            }
        } else {
            return GraceResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
        }
        log.info("path = " + path);
        String finalPath;
        if(StringUtils.isNoneBlank(path)) {
            finalPath = fileResource.getHost() + path;
        } else {
            return GraceResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        }
        return GraceResult.ok(finalPath);
    }

    @Override
    public GraceResult uploadSomeFiles(String userId, MultipartFile[] files) throws Exception {
        //声明list,用于存放多个图片的地址路径，返回到前端
        List<String> imageUrlList = new ArrayList<>();
        if(files != null && files.length > 0 ) {
            for(MultipartFile file : files) {
                String path;
                if(file != null) {
                    String fileName = file.getOriginalFilename();
                    //判断文件名不为空
                    if(StringUtils.isNoneBlank(fileName)) {
                        String[] fileNameArr = fileName.split("\\.");
                        //获得后缀
                        String suffix = fileNameArr[fileNameArr.length - 1];
                        //判断后缀符合我没的预定义规范
                        if(!"png".equalsIgnoreCase(suffix) &&
                        !"jpg".equalsIgnoreCase(suffix) &&
                                !"jpeg".equalsIgnoreCase(suffix)
                        ){
                            continue;
                        }
                        //上传
                        path = uploadService.uploadOSS(file,userId,suffix);
                    }else {
                        continue;
                    }
                }else {
                    continue;
                }
                String finalPath;
                if(StringUtils.isNoneBlank(path)) {
                    finalPath = fileResource.getOssHost() + path;
                    imageUrlList.add(finalPath);
                }
            }
        }

        return GraceResult.ok(imageUrlList);
    }
}
