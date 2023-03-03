package org.example.user.service.controller;


import lombok.extern.slf4j.Slf4j;
import org.example.common.constant.StatusCode;
import org.example.common.vo.ResultVo;
import org.example.user.service.model.User;
import org.example.user.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResultVo login(@RequestBody User user) {
        if (user == null || user.getUserName() == null || user.getPassword() == null) {
            return new ResultVo(StatusCode.FAIL, "请输入用户名密码");
        }
        return userService.login(user);
    }

    @PostMapping("/register")
    public ResultVo register(@RequestBody User user) {
        if (user == null || user.getUserName() == null || user.getPassword() == null) {
            return new ResultVo(StatusCode.FAIL, "请输入完整的注册信息");
        }
        return userService.register(user);
    }

}
