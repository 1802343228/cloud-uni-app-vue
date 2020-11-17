package com.soft1851.api.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;



/**
 * @author crq
 */
@Aspect
@Component
public class ServiceLogAspect {
    final static Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);

    @Around("execution(* com.soft1851.*service.impl..*.*(..))")
    public Object recordTimeOfService(ProceedingJoinPoint joinPoint)throws Throwable {
        logger.info("==== 开始执行 {}.{}====",
                joinPoint.getTarget().getClass(),
                joinPoint.getSignature().getName()
                );
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();

        long end = System.currentTimeMillis();
        long takeTime = end - start;

        if(takeTime > 3000) {
            logger.error("当前执行耗时：{}",takeTime);
        } else if(takeTime > 2000) {
            logger.warn("当前执行耗时：{}",takeTime);
        }else {
            logger.info("当前执行耗时：{}",takeTime);
        }
        return result;
    }
}
