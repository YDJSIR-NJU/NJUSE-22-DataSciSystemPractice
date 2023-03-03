package org.example.comment.service.model;

import lombok.Data;

import java.io.Serializable;

/**
 * comment
 *
 * @author
 */
@Data
public class Comment implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer commentId;
    private Integer userId;
    private String comment;
}