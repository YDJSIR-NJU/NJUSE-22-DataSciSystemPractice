package com.example.backend.controller;

import com.example.backend.po.Logmanagementsystem;
import com.example.backend.service.QueryService;
import com.example.backend.util.Constant;
import com.example.backend.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/query")
public class QueryController {

    @Autowired
    QueryService queryService;

    @GetMapping("/rangeFilter")
    public ResultVo<List<Logmanagementsystem>> rangeFilter(@RequestParam Long start, @RequestParam Long end,
                                                           @RequestParam String column, @RequestParam String name) {
        List<Logmanagementsystem> res = queryService.rangeFilter(start, end, column, name);
        return new ResultVo<>(Constant.SUCCESS, String.valueOf(res.size()), res);
    }

    @GetMapping("/rangeFilterCount")
    public ResultVo<Integer> rangeFilterCount(@RequestParam Long start, @RequestParam Long end,
                                                           @RequestParam String column, @RequestParam String name) {
        Integer res = queryService.rangeFilterCount(start, end, column, name);
        return new ResultVo<>(Constant.SUCCESS, String.valueOf(res), res);
    }

    @GetMapping("/uuid")
    public ResultVo<Logmanagementsystem> getByUUid(@RequestParam String uuid) {
        Logmanagementsystem res = queryService.getByUuid(uuid);
        if (res == null) {
            return new ResultVo<>(Constant.FAIL, "0", null);
        } else {
            return new ResultVo<>(Constant.SUCCESS, "1", res);
        }
    }
}
