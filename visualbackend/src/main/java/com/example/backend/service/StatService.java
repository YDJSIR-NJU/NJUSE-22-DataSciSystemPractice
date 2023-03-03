package com.example.backend.service;

import com.example.backend.dao.LogmanagementsystemMapper;
import com.example.backend.po.CountItem;
import com.example.backend.po.Logmanagementsystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StatService {
    @Autowired
    LogmanagementsystemMapper logmanagementsystemMapper;

    public List<CountItem> getCountTotal(String column) {
        List<CountItem> res;
        switch (column) {
            case "appName":
                res = logmanagementsystemMapper.getCountTotalByAppName();
                break;
            case "containerId":
                res = logmanagementsystemMapper.getCountTotalByContainerId();
                break;
            case "containerName":
                res = logmanagementsystemMapper.getCountTotalByContainerName();
                break;
            case "level":
                res = logmanagementsystemMapper.getCountTotalByLevel();
                break;
            default:
                res = new ArrayList<>();
        }
        return res;
    }

    public List<CountItem> getCountByLevel(String column, String name) {
        List<CountItem> res;
        switch (column) {
            case "appName":
                res = logmanagementsystemMapper.getCountLevelByAppName(name);
                break;
            case "containerId":
                res = logmanagementsystemMapper.getCountLevelByContainerId(name);
                break;
            case "containerName":
                res = logmanagementsystemMapper.getCountLevelByContainerName(name);
                break;
            default:
                res = logmanagementsystemMapper.getCountTotalByLevel();
        }
        return res;
    }

    public Logmanagementsystem savingSum() {
        return logmanagementsystemMapper.selectTest();
    }
}
