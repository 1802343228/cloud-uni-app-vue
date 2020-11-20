package com.soft1851.admin.service;

import com.soft1851.pojo.AdminUser;

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
}
