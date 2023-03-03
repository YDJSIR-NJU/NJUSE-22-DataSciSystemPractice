package org.example.like.service.service;

import lombok.extern.slf4j.Slf4j;
import org.example.common.constant.StatusCode;
import org.example.common.util.TokenThreadLocalUtil;
import org.example.common.vo.CommentVo;
import org.example.common.vo.ResultVo;
import org.example.like.service.clients.CommentRestTemplateClient;
import org.example.like.service.dao.CommentLikesDao;
import org.example.like.service.model.CommentLikesKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LikeService {

    @Autowired
    CommentLikesDao commentLikesDao;

    @Autowired
    CommentRestTemplateClient commentRestTemplateClient;

    public ResultVo likeComment(CommentLikesKey commentLikesKey) {
        CommentVo comment = commentRestTemplateClient.getComment(commentLikesKey.getCommentId());
        if (comment == null) {
            log.error("User ID: {} tries to like an not-existent comment ID: {}", TokenThreadLocalUtil.get().getUserId(), commentLikesKey.getCommentId());
            return new ResultVo(StatusCode.FAIL, "You are trying to like a not-existent comment!");
        }

        if (commentLikesDao.selectByPrimaryKey(commentLikesKey.getCommentId(), commentLikesKey.getUserId()) != null) {
            //取消点赞
            if (commentLikesDao.deleteByPrimaryKey(commentLikesKey) > 0) {
                log.info("User ID: {} cancels like on comment: {}", commentLikesKey.getUserId(), commentLikesKey.getCommentId());
                return new ResultVo(StatusCode.SUCCESS, "You cancelled like on comment " + commentLikesKey.getCommentId());
            }
        } else {
            if (commentLikesDao.insert(commentLikesKey) > 0) {
                log.info("User ID: {} likes comment: {}", commentLikesKey.getUserId(), commentLikesKey.getCommentId());
                return new ResultVo(StatusCode.SUCCESS, "You likes comment " + commentLikesKey.getCommentId());
            }
        }
        return new ResultVo(StatusCode.FAIL, "Failure");
    }
}
