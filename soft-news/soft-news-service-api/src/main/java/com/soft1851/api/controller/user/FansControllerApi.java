package com.soft1851.api.controller.user;

import com.soft1851.result.GraceResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author crq
 */

@Api(value = "粉丝相关Controller",tags = {"粉丝相关Controller"})
@RequestMapping("fans")
public interface FansControllerApi {
    /**
     * 获取所有用户信息
     * @return
     */
    @ApiOperation(value = "获得所有粉丝",notes = "获得所有粉丝",httpMethod = "POST")
    @PostMapping("/follow")
    GraceResult getFollow();
}
