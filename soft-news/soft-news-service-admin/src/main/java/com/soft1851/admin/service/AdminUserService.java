package com.soft1851.admin.service;

import com.soft1851.pojo.AdminUser;
import com.soft1851.pojo.bo.NewAdminBO;

/**
 * @author crq
 */
public interface AdminUserService {
    /**
     * 获得管理员用户信息
     * @param username
     * @return
     */
    AdminUser queryAdminByUsername(String username);

    /**
     * 新增管理员
     * @param newAdminBO
     */
    void createAdminUser(NewAdminBO newAdminBO);
}
