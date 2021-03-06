package com.soft1851.user.controller;

import com.soft1851.api.BaseController;
import com.soft1851.api.controller.user.UserControllerApi;
import com.soft1851.pojo.AppUser;
import com.soft1851.pojo.bo.UpdateUserInfoBO;
import com.soft1851.pojo.vo.AppUserVO;
import com.soft1851.pojo.vo.UserAccountInfoVO;
import com.soft1851.result.GraceResult;
import com.soft1851.result.ResponseStatusEnum;
import com.soft1851.user.service.UserService;
import com.soft1851.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author crq
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController extends BaseController implements UserControllerApi {

    private final UserService userService;


    @Override
    public GraceResult getAllUsers() {
        return null;
    }


    @Override
    public GraceResult getUserInfo(String userId) {

        if(StringUtils.isBlank(userId)) {
            return GraceResult.errorCustom(ResponseStatusEnum.UN_LOGIN);
        }
        AppUser user = getUser(userId);
        UserAccountInfoVO accountVO = new UserAccountInfoVO();
        BeanUtils.copyProperties(user,accountVO);
        return GraceResult.ok(accountVO);

    }

    @Override
    public GraceResult updateUserInfo(@Valid UpdateUserInfoBO updateUserInfoBO, BindingResult result) {
        if(result.hasErrors()) {
            Map<String,String> errorMap = getErrors(result);
            return GraceResult.errorMap(errorMap);
        }
        userService.updateUserInfo(updateUserInfoBO);
        return GraceResult.ok();
    }

    @Override
    public GraceResult getUserBasicInfo(String userId) {
        if(StringUtils.isBlank(userId)) {
            return GraceResult.errorCustom(ResponseStatusEnum.UN_LOGIN);
        }
        AppUser user = getUser(userId);
        AppUserVO userVO = new AppUserVO();
        BeanUtils.copyProperties(user,userVO);
        return GraceResult.ok(userVO);
    }

    @Override
    public GraceResult queryByIds(String userIds) {
        if(StringUtils.isBlank(userIds)) {
            return GraceResult.errorCustom(ResponseStatusEnum.USER_NOT_EXIST_ERROR);
        }
        List<AppUserVO> publisherList = new ArrayList<>();
        List<String> userIdList = JsonUtil.jsonToList(userIds,String.class);
        System.out.println("88888888888888");
        System.out.println(userIdList);
        assert userIdList != null;
        for(String userId:userIdList) {
            //获得用户基本信息
            AppUserVO userVO = getBasicUserInfo(userId);
            //添加到publisherList
            publisherList.add(userVO);
        }
        return GraceResult.ok(publisherList);
    }

    private AppUserVO getBasicUserInfo(String userId) {
        //根据Userid查询用户的信息
        AppUser user = getUser(userId);
        //返回用户信息
        AppUserVO userVO = new AppUserVO();
        BeanUtils.copyProperties(user,userVO);
        return userVO;
    }


    private AppUser getUser(String userId) {
        String userJson = redis.get(REDIS_USER_INFO + ":" + userId);
        AppUser user;
        if(StringUtils.isNoneBlank(userJson)) {
            user = JsonUtil.jsonToPojo(userJson,AppUser.class);
        }else {
            user = userService.getUser(userId);
            redis.set(REDIS_USER_INFO + ":" +userId,JsonUtil.objectToJson(user),1);
        }
        return user;
    }

}
