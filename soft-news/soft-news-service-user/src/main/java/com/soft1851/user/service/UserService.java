package com.soft1851.user.service;

import com.soft1851.pojo.AppUser;

/**
 * @author crq
 */
public interface UserService {
    /**
     * 判断用户是否存在，如果存在返回User信息
     * @param mobile
     * @return
     */
    AppUser queryMobileIsExist(String mobile);

    /**
     * 创建用户，新增用户记录到数据库
     * @param mobile
     * @return
     */
    AppUser createUser(String mobile);
}
