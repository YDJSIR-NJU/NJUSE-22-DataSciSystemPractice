package com.example.backend.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private Integer count;
}
