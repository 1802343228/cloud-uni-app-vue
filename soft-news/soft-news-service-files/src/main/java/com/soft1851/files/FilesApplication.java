package com.soft1851.files;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author crq
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(basePackages = {"com.soft1851","org.n3r.idworker"})
public class FilesApplication {
    public static void main(String[] args) {
        SpringApplication.run(FilesApplication.class,args);
    }
}
