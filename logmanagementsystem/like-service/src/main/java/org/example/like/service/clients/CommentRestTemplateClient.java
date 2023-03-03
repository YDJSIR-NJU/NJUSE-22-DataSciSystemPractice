package org.example.like.service.clients;

import org.example.common.util.JwtUtil;
import org.example.common.vo.CommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class CommentRestTemplateClient {

    @Autowired
    RestTemplate restTemplate;

    public CommentVo getComment(Integer commentId) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtUtil.TOKEN_HEADER, request.getHeader(JwtUtil.TOKEN_HEADER));

        ResponseEntity<CommentVo> restExchange =
                restTemplate.exchange(
                        "http://zuulservice/commentservice/comment/getComment/{commentId}",
                        HttpMethod.GET, new HttpEntity<>(headers), CommentVo.class, commentId);
        return restExchange.getBody();
    }
}
