package org.example.like.service.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.like.service.model.CommentLikesKey;

@Mapper
public interface CommentLikesDao {
    int deleteByPrimaryKey(CommentLikesKey key);

    int insert(CommentLikesKey record);

    int insertSelective(CommentLikesKey record);

    CommentLikesKey selectByPrimaryKey(@Param("commentId") Integer commentId, @Param("userId") Integer userId);
}