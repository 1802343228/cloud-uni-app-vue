package com.soft1851.admin.controller;

import com.soft1851.admin.service.AdminUserService;
import com.soft1851.api.BaseController;
import com.soft1851.api.controller.admin.AdminMsgControllerApi;
import com.soft1851.exception.GraceException;
import com.soft1851.pojo.AdminUser;
import com.soft1851.pojo.bo.AdminLoginBO;
import com.soft1851.pojo.bo.NewAdminBO;
import com.soft1851.result.GraceResult;
import com.soft1851.result.ResponseStatusEnum;
import com.soft1851.utils.PageGridResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author crq
 */
@RestController
public class AdminMsgController extends BaseController implements AdminMsgControllerApi {
    @Autowired
    private AdminUserService adminUserService;

    @Override
    public GraceResult adminLogin(AdminLoginBO adminLoginBO, HttpServletRequest request, HttpServletResponse response) {
        //查询用户是否存在
        AdminUser admin = adminUserService.queryAdminByUsername(adminLoginBO.getUsername());
        if(admin == null) {
            return GraceResult.errorCustom(ResponseStatusEnum.ADMIN_NOT_EXIT_ERROR);
        }
        //判断密码是否匹配
        boolean isPwdMatch = BCrypt.checkpw(adminLoginBO.getPassword(),admin.getPassword());
        if(isPwdMatch) {
            doLoginSettings(admin,request,response);
            return GraceResult.ok();
        }else {
            //密码不匹配
            return GraceResult.errorCustom(ResponseStatusEnum.ADMIN_NOT_EXIT_ERROR);
        }
    }

    @Override
    public GraceResult adminIsExist(String username) {
        checkAdminExist(username);
        return GraceResult.ok();
    }

    @Override
    public GraceResult addNewAdmin(HttpServletResponse response, HttpServletRequest request, NewAdminBO newAdminBO) {
        //base64不为空，则代表人脸入库，否则需要用户输入密码和确认密码
        if(StringUtils.isBlank(newAdminBO.getImg64())) {
            if(StringUtils.isBlank(newAdminBO.getPassword()) ||
            StringUtils.isBlank(newAdminBO.getConfirmPassword())
            ) {
                return GraceResult.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_NULL_ERROR);
            }
        }
        //密码不为空 则必须判断俩次输入一致
        if(StringUtils.isNotBlank(newAdminBO.getPassword())) {
            if(!newAdminBO.getPassword().equalsIgnoreCase(newAdminBO.getConfirmPassword())) {
                return GraceResult.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_ERROR);
            }
        }
        //校验用户名唯一
        checkAdminExist(newAdminBO.getUsername());
        //调用service存入admin信息
        adminUserService.createAdminUser(newAdminBO);
        return GraceResult.ok();
    }

    @Override
    public GraceResult getAdminList(Integer page, Integer pageSize) {
        if(page == null) {
            page = COMMON_START_PAGE;
        }
        if(pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }
        PageGridResult result = adminUserService.queryAdminList(page,pageSize);
        return GraceResult.ok(result);
    }

    @Override
    public GraceResult adminLogout(String adminId, HttpServletRequest request, HttpServletResponse response) {
        //从redis中删除admin的会话token
        redis.del(REDIS_ADMIN_TOKEN+":"+adminId);
        //从cookie中清理admin登录相关信息
        deleteCookie(request,response,"aToken");
        deleteCookie(request,response,"aId");
        deleteCookie(request,response,"aName");
        return GraceResult.ok();
    }

    @Override
    public GraceResult updateAdmin(HttpServletRequest request, HttpServletResponse response, NewAdminBO newAdminBO) {
        adminUserService.updateAdmin(newAdminBO.getUsername(), newAdminBO.getFaceId());
        return GraceResult.ok(newAdminBO);
    }

    private void checkAdminExist(String username) {
        AdminUser admin = adminUserService.queryAdminByUsername(username);
        if(admin != null) {
            GraceException.display(ResponseStatusEnum.ADMIN_USERNAME_EXIST_ERROR);
        }
    }


    private void doLoginSettings(AdminUser admin,HttpServletRequest request,HttpServletResponse response) {
        //保存token到redis
        String token = UUID.randomUUID().toString();
        redis.set(REDIS_ADMIN_TOKEN + ":" + admin.getId(),token);
        //保存admin登录基本token信息到cookie
        setCookie(request,response,"aToken",token,COOKIE_MONTH);
        setCookie(request,response,"aId",admin.getId(),COOKIE_MONTH);
        setCookie(request,response,"aName",admin.getAdminName(),COOKIE_MONTH);
    }


    public static void main(String[] args) {
        String pwd = BCrypt.hashpw("123123", BCrypt.gensalt());
        System.out.println(pwd);
    }
}
