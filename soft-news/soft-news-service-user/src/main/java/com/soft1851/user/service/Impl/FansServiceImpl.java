package com.soft1851.user.service.Impl;

import com.soft1851.api.service.BaseService;
import com.soft1851.pojo.AppUser;
import com.soft1851.pojo.Fans;
import com.soft1851.user.mapper.FansMapper;
import com.soft1851.user.service.FansService;
import com.soft1851.user.service.UserService;
import com.soft1851.utils.RedisOperator;
import lombok.RequiredArgsConstructor;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author crq
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FansServiceImpl extends BaseService implements FansService {
    private final FansMapper fansMapper;
    private final Sid sid;
    private final UserService userService;
    private final RedisOperator redis;

    @Override
    public boolean isMeFollowThisWriter(String writerId, String fanId) {
        Fans fans = new Fans();
        fans.setFanId(fanId);
        fans.setWriterId(writerId);
        int count = fansMapper.selectCount(fans);
        return count > 0;
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void follow(String writerId, String fanId) {
        //获得粉丝用户信息
        AppUser fanInfo = userService.getUser(fanId);
        String fanPkId = sid.nextShort();
        //保存作者和粉丝关联关系，字段冗余便于统计分析，并且只认第一次成为粉丝的数据
        Fans fan = new Fans();
        fan.setId(fanPkId);
        fan.setFanId(fanId);
        fan.setFace(fanInfo.getFace());
        fan.setWriterId(writerId);
        fan.setFanNickname(fanInfo.getNickname());
        fan.setProvince(fanInfo.getProvince());
        fan.setSex(fanInfo.getSex());
        fansMapper.insert(fan);
        //redis 作者粉丝数累加
        redis.increment(REDIS_WRITER_FANS_COUNTS + ":" +writerId,1);
        redis.increment(REDIS_MY_FOLLOW_COUNTS + ":" +fanId,1);
    }

    @Override
    public void unfollow(String writerId, String fanId) {
        Fans fans = new Fans();
        fans.setWriterId(writerId);
        fans.setFanId(fanId);
        fansMapper.delete(fans);
        redis.decrement(REDIS_WRITER_FANS_COUNTS+":"+writerId,1);
        redis.decrement(REDIS_MY_FOLLOW_COUNTS+":"+fanId,1);

    }
}
