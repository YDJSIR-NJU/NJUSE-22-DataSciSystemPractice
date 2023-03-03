package com.example.backend.service;

import com.example.backend.dao.LogmanagementsystemMapper;
import com.example.backend.po.Logmanagementsystem;
import com.example.backend.vo.TimeRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class ListService {
    @Autowired
    LogmanagementsystemMapper logmanagementsystemMapper;

    public Logmanagementsystem savingSum() {
        return logmanagementsystemMapper.selectTest();
    }

    public List<String> getEnum(String column) {
        List<String> res;
        switch (column) {
            case "level":
                res = logmanagementsystemMapper.getEnumLevel();
                break;
            case "class":
                res = logmanagementsystemMapper.getEnumClass();
                break;
            case "appName":
                res = logmanagementsystemMapper.getEnumAppName();
                break;
            case "containerId":
                res = logmanagementsystemMapper.getEnumContainerId();
                break;
            case "containerName":
                res = logmanagementsystemMapper.getEnumContainerName();
                break;
            case "source":
                res = logmanagementsystemMapper.getEnumSource();
                break;
            default:
                res = new LinkedList<>();
        }
        return res;
    }

    public TimeRange getMaxTimeRange() {
        TimeRange res = new TimeRange();
        Long minTime = logmanagementsystemMapper.getMinTimestamp();
        Long maxTime = logmanagementsystemMapper.getMaxTimestamp();
        res.setMinTime(minTime);
        res.setMaxTime(maxTime);
        res.setMinTimeStr(String.valueOf(minTime));
        res.setMaxTimeStr(String.valueOf(maxTime));
        return res;
    }
}
