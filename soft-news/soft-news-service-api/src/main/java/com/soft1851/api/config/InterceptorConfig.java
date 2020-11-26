package com.soft1851.api.config;

import com.soft1851.api.interceptors.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author crq
 */

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Bean
    public PassportInterceptor passportInterceptor() {
        return new PassportInterceptor();
    }

    @Bean
    public UserTokenInterceptor userTokenInterceptor() {
        return new UserTokenInterceptor();
    }

    @Bean
    public UserActiveInterceptor userActiveInterceptor() {
        return new UserActiveInterceptor();
    }

    @Bean
    public AdminTokenInterceptor adminTokenInterceptor() {
        return new AdminTokenInterceptor();
    }

    @Bean
    public ArticleReadInterceptor articleReadInterceptor() {
        return new ArticleReadInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor())
                .addPathPatterns("/passport/smsCode");

        registry.addInterceptor(userTokenInterceptor())
                .addPathPatterns("/user/userBasicInfo")
                .addPathPatterns("/user/updateUserInfo");
//                .addPathPatterns("/fs/uploadFace")
//                .addPathPatterns("/fs/uploadSomeFiles");

//        registry.addInterceptor(userActiveInterceptor())
//                .addPathPatterns("/fans/follow");

        registry.addInterceptor(adminTokenInterceptor())
                .addPathPatterns("/adminMsg/adminIsExist");

        registry.addInterceptor(articleReadInterceptor())
                .addPathPatterns("/article/readArticle");
    }
}
