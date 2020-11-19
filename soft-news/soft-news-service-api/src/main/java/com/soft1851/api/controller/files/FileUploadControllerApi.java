package com.soft1851.api.controller.files;

import com.soft1851.result.GraceResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author crq
 */
@Api(value = "文件上传controller",tags = {"文件上传controller"})
@RequestMapping("fs")
public interface FileUploadControllerApi {

    /**
     * 上传头像
     * @param userId
     * @param file
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "上传用户头像",notes = "上传用户头像",httpMethod = "POST")
    @PostMapping("uploadFace")
    GraceResult uploadFace(@RequestParam String userId, MultipartFile file)throws Exception;


    /**
     * 上传多个文件
     * @param userId
     * @param files
     * @return
     * @throws Exception
     */
    @PostMapping("/uploadSomeFiles")
    GraceResult uploadSomeFiles(@RequestParam String userId,
                                MultipartFile[] files
                                )throws Exception;
}
