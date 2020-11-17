package com.soft1851.api.interceptors;

import com.soft1851.exception.GraceException;
import com.soft1851.result.ResponseStatusEnum;
import com.soft1851.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author crq
 */
public class BaseInterceptor {
    @Autowired
    private RedisOperator redis;

    public static final String REDIS_USER_TOKEN = "redis_user_token";
    public static final String REDIS_ADMIN_TOKEN = "redis_admin_token";

    public boolean verifyUserIdToken(String id, String token, String redisKeyPrefix) {
        if (StringUtils.isNotBlank(id) && StringUtils.isNotBlank(token)) {
            String uniqueToken = redis.get(redisKeyPrefix + ":" + id);
            if (StringUtils.isBlank(uniqueToken)) {
                GraceException.display(ResponseStatusEnum.UN_LOGIN);
                return false;
            } else {
                if (!uniqueToken.equals(token)) {
                    GraceException.display(ResponseStatusEnum.TICKET_INVALID);
                    return false;
                }
            }
        } else {
            GraceException.display(ResponseStatusEnum.UN_LOGIN);
            return false;
        }

        // false: 请求被拦截，被驳回，验证出现问题;true: 请求在经过验证校验以后，确认是可以放行的
        return true;
    }
}
