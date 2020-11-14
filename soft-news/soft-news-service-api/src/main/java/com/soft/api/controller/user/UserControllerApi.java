package com.soft.api.controller.user;

import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author crq
 */
public interface UserControllerApi {

    /**
     * 获取所有用户
     * @return
     */
    @GetMapping("/users")
    Object getUsers();
}
