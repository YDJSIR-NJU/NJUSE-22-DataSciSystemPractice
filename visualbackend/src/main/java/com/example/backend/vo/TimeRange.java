package com.example.backend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeRange {
    public Long minTime;
    public Long maxTime;
    public String minTimeStr;
    public String maxTimeStr;
}
