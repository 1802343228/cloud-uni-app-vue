package com.soft1851.api.controller.user;

import com.soft1851.pojo.bo.RegistLoginBO;
import com.soft1851.result.GraceResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author crq
 */
@Api(value = "用户注册登录",tags = {"用户注册登录的Controller"})
@RequestMapping("passport")
public interface PassportControllerApi {
    /**
     * 发送短信
     * @param mobile
     * @param request
     * @return
     */
    @ApiOperation(value = "获取短信验证码",notes = "获得短信验证码",httpMethod = "GET")
    @GetMapping("/smsCode")
    GraceResult getCode(@RequestParam String mobile, HttpServletRequest request);

    /**
     * 一键注册登录接口
     * @param registLoginBO
     * @param result
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value = "一键注册登录接口",notes = "一键注册登录接口",httpMethod = "POST")
    @PostMapping("/sign")
    GraceResult doSign(@RequestBody @Valid RegistLoginBO registLoginBO,
                       BindingResult result,
                       HttpServletRequest request,
                       HttpServletResponse response);

    /**
     * 退出登录
     * @param request
     * @param response
     * @param userId
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation(value = "用户退出登录",notes = "用户退出登录",
    httpMethod = "POST"
    )
    GraceResult logout(HttpServletRequest request,
            HttpServletResponse response,
                       @RequestParam String userId
                       );

}
