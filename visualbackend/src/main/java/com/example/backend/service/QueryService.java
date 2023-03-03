package com.example.backend.service;

import com.example.backend.dao.LogmanagementsystemMapper;
import com.example.backend.po.Logmanagementsystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QueryService {
    @Autowired
    LogmanagementsystemMapper logmanagementsystemMapper;

    public List<Logmanagementsystem> rangeFilter(Long start, Long end, String column, String name) {
        List<Logmanagementsystem> res;
        switch (column) {
            case "all":
                if(logmanagementsystemMapper.selectRangeCount(start, end) > 10000){
                    res = new ArrayList<>();
                    break;
                }
                res = logmanagementsystemMapper.selectRange(start, end); // 不附加条件
                break;
            case "appName":
                if(logmanagementsystemMapper.selectRangeByAppNameCount(start, end, name) > 10000){
                    res = new ArrayList<>();
                    break;
                }
                res = logmanagementsystemMapper.selectRangeByAppName(start, end, name);
                break;
            case "containerId":
                if(logmanagementsystemMapper.selectRangeByContainerIdCount(start, end, name) > 10000){
                    res = new ArrayList<>();
                    break;
                }
                res = logmanagementsystemMapper.selectRangeByContainerId(start, end, name);
                break;
            case "containerName":
                if(logmanagementsystemMapper.selectRangeByContainerNameCount(start, end, name) > 10000){
                    res = new ArrayList<>();
                    break;
                }
                res = logmanagementsystemMapper.selectRangeByContainerName(start, end, name);
                break;
            default:
                res = new ArrayList<>();
        }
        return res;
    }

    public Integer rangeFilterCount(Long start, Long end, String column, String name) {
        Integer res;
        switch (column) {
            case "all":
                res = logmanagementsystemMapper.selectRangeCount(start, end); // 不附加条件
                break;
            case "appName":
                res = logmanagementsystemMapper.selectRangeByAppNameCount(start, end, name);
                break;
            case "containerId":
                res = logmanagementsystemMapper.selectRangeByContainerIdCount(start, end, name);
                break;
            case "containerName":
                res = logmanagementsystemMapper.selectRangeByContainerNameCount(start, end, name);
                break;
            default:
                res = Integer.MAX_VALUE;
        }
        return res;
    }

    public Logmanagementsystem getByUuid(String uuid) {
        return logmanagementsystemMapper.selectByUuid(uuid);
    }

    public Logmanagementsystem savingSum() {
        return logmanagementsystemMapper.selectTest();
    }
}
