package com.example.backend.controller;

import com.example.backend.service.ListService;
import com.example.backend.util.Constant;
import com.example.backend.vo.ResultVo;
import com.example.backend.vo.TimeRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/list")
public class ListController {

    @Autowired
    ListService listService;

    @GetMapping("/enum")
    public ResultVo<List<String>> getEnum(@RequestParam String column) {
        List<String> res = listService.getEnum(column);
        return new ResultVo<>(Constant.SUCCESS, String.valueOf(res.size()), res);
    }

    @GetMapping("/timeRange")
    public ResultVo<TimeRange> getMaxTimeRange() {
        return new ResultVo<>(Constant.SUCCESS, "1", listService.getMaxTimeRange());
    }

}
