package com.soft1851.utils;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.soft1851.utils.extend.AliyunResource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author crq
 */
@Component
public class SmsUtil {

    @Resource
    public AliyunResource aliyunResource;
    public void sendSms(String mobile,String code) {
        DefaultProfile profile = DefaultProfile.getProfile("ch-hangzhou",
                aliyunResource.getAccessKeyId(),
                aliyunResource.getAccessKeySecret());
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId","ch-hangzhou");
        request.putQueryParameter("PhoneNumbers",mobile);
        request.putQueryParameter("SignName","智慧园区");
        request.putQueryParameter("TemplateCode","SMS_190277609");
        request.putQueryParameter("TemplateParam","{\"code\":\""+code+"\"}");

        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        }catch (ClientException e){
            System.err.println(e.getMessage());
        }

    }
}
