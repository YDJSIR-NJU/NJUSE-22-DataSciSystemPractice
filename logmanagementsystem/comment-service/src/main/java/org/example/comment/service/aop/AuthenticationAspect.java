package org.example.comment.service.aop;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.example.common.util.JwtUtil;
import org.example.common.util.TokenThreadLocalUtil;
import org.example.common.vo.UserVo;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Aspect
@Component
@Slf4j
@Order(1500)
public class AuthenticationAspect {

    @Pointcut("execution(public * org.example.comment.service.controller.*.*(..))")
    private void authentication() {
    }


    @Around("authentication()")
    public Object authenticationHandler(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result;

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();

        String token = request.getHeader(JwtUtil.TOKEN_HEADER);
        // 有token我就记录token，没token还能到这里说明是白名单
        if (token != null && token.length() != 0) {
            Claims claims = JwtUtil.getClaimsFromToken(token);
            if (claims == null) {
                //token解析失败
                throw new RuntimeException("Token: " + token + " Resolve Failure!");
            }
            UserVo userVo = new UserVo();
            userVo.setUserId(JwtUtil.getUserIdFromClaims(claims));
            userVo.setUserName(JwtUtil.getUserNameFromClaims(claims));
            TokenThreadLocalUtil.set(userVo);

            log.info("Token validated. Visiting URL: {} with token: {}"
                    , request.getRequestURL()
                    , token);
        }
        result = proceedingJoinPoint.proceed();
        TokenThreadLocalUtil.remove();
        return result;
    }

}
