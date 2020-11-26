package com.soft1851.user.controller;

import com.soft1851.api.controller.user.FansControllerApi;
import com.soft1851.result.GraceResult;
import com.soft1851.user.service.FansService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author crq
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FansController implements FansControllerApi {
    @Resource
    private final FansService fansService;


    @Override
    public GraceResult isMeFollowThisWriter(String writerId, String fanId) {
        boolean result = fansService.isMeFollowThisWriter(writerId, fanId);
        return GraceResult.ok(result);
    }

    @Override
    public GraceResult follow(String writerId, String fanId) {
        //判断不为空
        fansService.follow(writerId, fanId);
        return GraceResult.ok();
    }

    @Override
    public GraceResult unfollow(String writerId, String fanId) {
        fansService.unfollow(writerId, fanId);
        return GraceResult.ok();
    }
}
