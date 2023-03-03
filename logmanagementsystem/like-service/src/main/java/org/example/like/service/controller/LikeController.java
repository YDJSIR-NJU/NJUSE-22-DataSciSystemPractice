package org.example.like.service.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.common.util.TokenThreadLocalUtil;
import org.example.common.vo.ResultVo;
import org.example.like.service.model.CommentLikesKey;
import org.example.like.service.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/like")
@Slf4j
public class LikeController {

    @Autowired
    LikeService likeService;

    @PostMapping("/")
    public ResultVo likeComment(@RequestBody CommentLikesKey commentLikesKey) {
        commentLikesKey.setUserId(TokenThreadLocalUtil.get().getUserId());
        return likeService.likeComment(commentLikesKey);
    }

}
