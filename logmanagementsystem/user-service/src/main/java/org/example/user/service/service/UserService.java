package org.example.user.service.service;

import lombok.extern.slf4j.Slf4j;
import org.example.common.constant.StatusCode;
import org.example.common.util.JwtUtil;
import org.example.common.vo.ResultVo;
import org.example.common.vo.UserVo;
import org.example.user.service.dao.UserDao;
import org.example.user.service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    PasswordEncoder passwordEncoder;

    public ResultVo login(User user) {
        User userInDb = userDao.selectByUserName(user.getUserName());
        if (userInDb == null) {
            throw new RuntimeException("User not exists!");
        }

        if (passwordEncoder.matches(user.getPassword(), userInDb.getPassword())) {
            log.info("User ID: {} logins", user);
            UserVo userVo = new UserVo();
            userVo.setUserId(userInDb.getUserId());
            userVo.setUserName(userInDb.getUserName());
            String token = JwtUtil.generateTokenByUser(userVo);
            return new ResultVo(StatusCode.SUCCESS, token);
        } else {
            throw new RuntimeException("Your account or password is wrong!");
        }
    }


    public ResultVo register(User user) {
        User userInDb = userDao.selectByUserName(user.getUserName());
        if (userInDb != null) {
            throw new RuntimeException("Already exists!");
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userDao.insert(user);
            log.info("User ID: {} registers successful", user);
            return new ResultVo(StatusCode.SUCCESS, "Register Success!");
        }
    }
}
