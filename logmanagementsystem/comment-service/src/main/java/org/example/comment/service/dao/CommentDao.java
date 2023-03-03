package org.example.comment.service.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.comment.service.model.Comment;

@Mapper
public interface CommentDao {
    int deleteByPrimaryKey(Integer commentId);

    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(Integer commentId);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);
}