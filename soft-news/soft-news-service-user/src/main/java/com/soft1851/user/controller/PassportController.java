package com.soft1851.user.controller;

import com.soft1851.api.controller.user.PassportControllerApi;
import com.soft1851.result.GraceResult;
import com.soft1851.utils.SmsUtil;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author crq
 */
@RestController
public class PassportController implements PassportControllerApi {
    @Resource
    private SmsUtil smsUtil;


    @Override
    public GraceResult getCode(String mobile, HttpServletRequest request) {
        String random = (int) ((Math.random() * 9 + 1)*100000) + "";
        System.out.println(random);
        smsUtil.sendSms(mobile,random);
        return GraceResult.ok();
    }
}
