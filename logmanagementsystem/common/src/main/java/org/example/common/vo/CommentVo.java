package org.example.common.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CommentVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer commentId;
    private Integer userId;
    private String comment;
}
