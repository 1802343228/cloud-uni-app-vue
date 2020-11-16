package com.soft1851.user.controller;


import com.soft1851.api.BaseController;
import com.soft1851.api.controller.user.PassportControllerApi;
import com.soft1851.enums.UserStatus;
import com.soft1851.pojo.AppUser;
import com.soft1851.pojo.bo.RegistLoginBO;
import com.soft1851.result.GraceResult;
import com.soft1851.result.ResponseStatusEnum;
import com.soft1851.user.service.UserService;
import com.soft1851.utils.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;
import java.util.UUID;

/**
 * @author crq
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PassportController extends BaseController implements PassportControllerApi {
    @Resource
    private SmsUtil smsUtil;
    private final UserService userService;

    @Override
    public GraceResult getCode(String mobile, HttpServletRequest request) {
        //获取用户ip
        String userIp = IpUtil.getRequestIp(request);
        //根据用户的ip进行限制，限制用户在60秒内只能获取一次验证码
        redis.setnx60s(MOBILE_SMSCODE + ":" + userIp,userIp);
        //生成随机验证码并且发送短信
        String random = (int) ((Math.random() * 9 + 1)*100000) + "";
        System.out.println(random);
        //验证码存入redis
        redis.set(MOBILE_SMSCODE +":" +mobile,random,30*60);
      //  smsUtil.sendSms(mobile,random);
        return GraceResult.ok();
    }



    @Override
    public GraceResult doSign(@Valid RegistLoginBO registLoginBO, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        //判断bingdingResult中是否保存了错误的验证信息，如果有，则需要返回
        if(result.hasErrors()) {
            Map<String,String> map = getErrors(result);
            return GraceResult.errorMap(map);
        }
        String mobile = registLoginBO.getMobile();
        String smsCode = registLoginBO.getSmsCode();

        //校验验证码是否匹配
        String redisSmsCode = redis.get(MOBILE_SMSCODE + ":" + mobile);
        if(StringUtils.isBlank(redisSmsCode) || !redisSmsCode.equalsIgnoreCase(smsCode)) {
            return GraceResult.errorCustom(ResponseStatusEnum.SMS_CODE_ERROR);

        }

        //查询数据库，判断该用户注册
        AppUser user = userService.queryMobileIsExist(mobile);
        if(user != null && user.getActiveStatus().equals(UserStatus.FROZEN.type)){
            //如果用户不为空，并且状态为冻结，则直接抛出异常，禁止登录
            return GraceResult.errorCustom(ResponseStatusEnum.USER_FROZEN);
        }else if (user == null) {
            user = userService.createUser(mobile);
        }

        //保存用户分布式会话的相关操作
        int userActiveStatus = user.getActiveStatus();
        if(userActiveStatus != UserStatus.FROZEN.type) {
            //保存token到redis
            String uToken = UUID.randomUUID().toString();
            redis.set(REDIS_USER_TOKEN + ":" + user.getId(),uToken);
            redis.set(REDIS_USER_INFO + ":" +user.getId(), JsonUtil.objectToJson(user));
            //保存用户ud和token到cookie中
            setCookie(request,response,"utoken",uToken,COOKIE_MONTH);
            setCookie(request,response,"uid",user.getId(),COOKIE_MONTH);
        }
        //用户登录或注册成功以后，需要删除redis中的短信验证码
        redis.del(MOBILE_SMSCODE + ":" + mobile);
        //返回用户状态
        return GraceResult.ok(userActiveStatus);
    }
}
