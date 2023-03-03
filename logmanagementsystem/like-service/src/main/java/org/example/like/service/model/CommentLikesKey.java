package org.example.like.service.model;

import lombok.Data;

import java.io.Serializable;

/**
 * comment_likes
 *
 * @author
 */
@Data
public class CommentLikesKey implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer commentId;
    private Integer userId;
}