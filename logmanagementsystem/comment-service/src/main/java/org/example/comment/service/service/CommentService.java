package org.example.comment.service.service;

import lombok.extern.slf4j.Slf4j;
import org.example.comment.service.dao.CommentDao;
import org.example.comment.service.model.Comment;
import org.example.common.constant.StatusCode;
import org.example.common.util.TokenThreadLocalUtil;
import org.example.common.vo.CommentVo;
import org.example.common.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CommentService {

    @Autowired
    CommentDao commentDao;

    public ResultVo publishComment(Comment comment) {
        if (commentDao.insert(comment) > 0) {
            log.info("User ID: {} releases comment: {} successful", comment.getUserId(), comment.getCommentId());
            return new ResultVo(StatusCode.SUCCESS, "Release Successful", comment);
        } else {
            log.warn("User ID: {} releases comment ID: {} in failure", comment.getUserId(), comment.getComment());
            return new ResultVo(StatusCode.FAIL, "Release Fail");
        }
    }

    public CommentVo getOneComment(Integer commentId) {
        Comment comment = commentDao.selectByPrimaryKey(commentId);
        if (comment != null) {
            CommentVo commentVo = new CommentVo();
            commentVo.setUserId(comment.getUserId());
            commentVo.setCommentId(commentId);
            commentVo.setComment(comment.getComment());
            log.info("User ID: {} get comment ID: {}", TokenThreadLocalUtil.get().getUserId(), commentId);
            return commentVo;
        } else {
            log.warn("Comment ID: {} not exists!", commentId);
            return null;
        }
    }
}
