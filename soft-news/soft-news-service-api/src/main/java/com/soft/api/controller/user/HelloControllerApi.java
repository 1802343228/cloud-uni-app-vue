package com.soft.api.controller.user;

import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author crq
 */
public interface HelloControllerApi {

    /**
     * hello接口
     * @return
     */
    @GetMapping("/hello")
    Object hello();
}
