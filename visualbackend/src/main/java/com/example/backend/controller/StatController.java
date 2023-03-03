package com.example.backend.controller;

import com.example.backend.po.CountItem;
import com.example.backend.service.StatService;
import com.example.backend.util.Constant;
import com.example.backend.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/stat")
public class StatController {

    @Autowired
    StatService statService;

    @GetMapping("/filterSum")
    public ResultVo<List<CountItem>> filterSum(@RequestParam String column) {
        List<CountItem> res = statService.getCountTotal(column);
        return new ResultVo<>(Constant.SUCCESS, String.valueOf(res.size()), res);
    }

    @GetMapping("/levelFilterSum")
    public ResultVo<List<CountItem>> levelFilterSum(@RequestParam String column, @RequestParam String name) {
        List<CountItem> res = statService.getCountByLevel(column, name);
        return new ResultVo<>(Constant.SUCCESS, String.valueOf(res.size()), res);
    }
}
