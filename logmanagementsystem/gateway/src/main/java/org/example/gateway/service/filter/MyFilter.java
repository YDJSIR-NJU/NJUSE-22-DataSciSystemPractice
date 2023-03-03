package org.example.gateway.service.filter;

import com.google.gson.Gson;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.example.common.constant.StatusCode;
import org.example.common.util.JwtUtil;
import org.example.common.vo.ResultVo;
import org.example.gateway.service.validation.TokenValidation;
import org.example.gateway.service.validation.UrlWhiteList;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class MyFilter extends ZuulFilter {

    private TokenValidation tokenValidation = new TokenValidation();

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        String uri = RequestContext.getCurrentContext().getRequest().getRequestURI();
        if (UrlWhiteList.allowUrl(uri)) {
            return false;
        }
        return true;
    }

    @Override
    public Object run() {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest httpServletRequest = currentContext.getRequest();
        HttpServletResponse httpServletResponse = currentContext.getResponse();

        boolean valid = tokenValidation.validate(httpServletRequest, httpServletResponse);

        if (!valid) {
            log.info("Invalid user request to URL: {} with token: {} from IP address: {}"
                    , httpServletRequest.getRequestURL()
                    , httpServletRequest.getHeader(TokenValidation.TOKEN_HEADER)
                    , httpServletRequest.getRemoteAddr());

            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json");
            try {
                httpServletResponse.getWriter().println(new Gson().toJson(new ResultVo(StatusCode.FAIL, "NO ACCESS!")));

                httpServletResponse.getWriter().flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            currentContext.addZuulRequestHeader(JwtUtil.TOKEN_HEADER, httpServletRequest.getHeader(JwtUtil.TOKEN_HEADER));
        }
        return null;
    }
}
