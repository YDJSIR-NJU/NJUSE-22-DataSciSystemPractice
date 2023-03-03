package org.example.comment.service.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.comment.service.model.Comment;
import org.example.comment.service.service.CommentService;
import org.example.common.util.TokenThreadLocalUtil;
import org.example.common.vo.CommentVo;
import org.example.common.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@Slf4j
public class CommentController {

    @Autowired
    CommentService commentService;

    @PostMapping("/publish")
    public ResultVo publishComment(@RequestBody Comment comment) {
        comment.setUserId(TokenThreadLocalUtil.get().getUserId());
        return commentService.publishComment(comment);
    }

    @GetMapping("/getComment/{commentId}")
    public CommentVo getOneComment(@PathVariable("commentId") Integer commentId) {
        return commentService.getOneComment(commentId);
    }
}
