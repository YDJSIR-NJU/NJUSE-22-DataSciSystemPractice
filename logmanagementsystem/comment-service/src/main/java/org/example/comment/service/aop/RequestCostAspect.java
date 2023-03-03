package org.example.comment.service.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(1000)
@Slf4j
public class RequestCostAspect {

    @Pointcut("execution(public * org.example.comment.service.controller.*.*(..))")
    public void requestCost() {
    }

    @Around("requestCost()")
    public Object logCost(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result;
        long enterTime = System.currentTimeMillis();
        result = proceedingJoinPoint.proceed();
        long exitTime = System.currentTimeMillis();
        log.info("{}.{} method execution cost {} ms with params {}", proceedingJoinPoint.getTarget().getClass().getSimpleName(), proceedingJoinPoint.getSignature().getName(), exitTime - enterTime, proceedingJoinPoint.getArgs());
        return result;
    }
}
