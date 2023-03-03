package org.example.user.service.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.example.common.constant.StatusCode;
import org.example.common.vo.ResultVo;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@Order(2000)
public class ExceptionHandleAspect {

    @Pointcut("execution(public * org.example.user.service.controller.*.*(..))")
    private void exception() {
    }


    @Around("exception()")
    public Object exceptionHandler(ProceedingJoinPoint proceedingJoinPoint) {
        Object result;
        try {
            result = proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            result = new ResultVo(StatusCode.FAIL, e.getMessage());
        }
        return result;
    }
}
