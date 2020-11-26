package com.soft1851.user.service;

/**
 * @author crq
 */
public interface FansService {
    /**
     * 查询当前用户是否关注作者
     * @param writerId
     * @param fanId
     * @return
     */
    boolean isMeFollowThisWriter(String writerId,String fanId);

    /**
     * 关注作者，成为粉丝
     * @param writerId
     * @param fanId
     */
    public void follow(String writerId,String fanId);
}
