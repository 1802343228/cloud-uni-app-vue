package com.soft1851.user.controller;

import com.soft.api.controller.user.HelloControllerApi;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author crq
 */
@RestController
public class HelloController implements HelloControllerApi {

   @Override
    public  Object hello(){
       return "hello";
   }
}
