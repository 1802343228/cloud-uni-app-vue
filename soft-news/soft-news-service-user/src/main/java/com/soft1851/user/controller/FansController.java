package com.soft1851.user.controller;

import com.soft1851.api.controller.user.FansControllerApi;
import com.soft1851.result.GraceResult;
import com.soft1851.user.mapper.FansMapper;
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
    private FansMapper fansMapper;

    @Override
    public GraceResult getFollow() {
        return GraceResult.ok(fansMapper.selectAll());
    }
}
