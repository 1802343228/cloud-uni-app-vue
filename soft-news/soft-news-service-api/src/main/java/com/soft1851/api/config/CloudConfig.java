package com.soft1851.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author crq
 */
@Configuration
public class CloudConfig {
    public CloudConfig() {

    }

    /**
     * 基于okHttp3配置RestTemplate
     * @return
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }
}
