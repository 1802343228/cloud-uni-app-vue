package com.soft1851.user.controller;

import com.soft1851.api.controller.user.HelloControllerApi;
import com.soft1851.result.GraceResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author crq
 */
@RestController
@Slf4j
public class HelloController implements HelloControllerApi {

   @Override
    public  Object hello(){
       log.info("info:hello");
       return GraceResult.ok("hello");
   }
}
