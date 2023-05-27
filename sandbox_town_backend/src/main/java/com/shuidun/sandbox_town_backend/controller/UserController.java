package com.shuidun.sandbox_town_backend.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.shuidun.sandbox_town_backend.bean.RestResponse;
import com.shuidun.sandbox_town_backend.bean.User;
import com.shuidun.sandbox_town_backend.enumeration.StatusCodeEnum;
import com.shuidun.sandbox_town_backend.exception.BusinessException;
import com.shuidun.sandbox_town_backend.service.UserService;
import com.shuidun.sandbox_town_backend.utils.MySaTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public RestResponse<?> login(String username, String passwd, boolean rememberMe) {
        // 判断是否已经登陆
        if (StpUtil.isLogin()) {
            throw new BusinessException(StatusCodeEnum.ALREADY_LOGGED_IN);
        }
        User user = userService.findUserByName(username);
        // 判断用户是否存在
        if (user == null) {
            throw new BusinessException(StatusCodeEnum.USER_NOT_EXIST);
        }
        // 判断密码是否正确
        String encryptedPasswd = MySaTokenUtils.encryptedPasswd(passwd, user.getSalt());
        if (encryptedPasswd.equals(user.getPassword())) {
            StpUtil.login(username, rememberMe);
            log.info("{} login success, rememberMe: {}", StpUtil.getLoginId(), rememberMe);
            return new RestResponse<>(StatusCodeEnum.SUCCESS);
        } else {
            throw new BusinessException(StatusCodeEnum.INCORRECT_CREDENTIALS);
        }
    }

    @PostMapping("/signup")
    public RestResponse<?> signup(String username, String passwd) {
        // 判断是否已经登陆
        if (StpUtil.isLogin()) {
            throw new BusinessException(StatusCodeEnum.ALREADY_LOGGED_IN);
        }
        // 判断密码强度
        if (passwd == null || passwd.length() < 6) {
            throw new BusinessException(StatusCodeEnum.PASSWORD_TOO_SHORT);
        }
        // 生成盐和加密后的密码
        String[] saltAndPasswd = MySaTokenUtils.generateSaltedHash(passwd);
        try {
            User user = new User(username, saltAndPasswd[1], saltAndPasswd[0]);
            userService.insertUser(user);
            StpUtil.login(username);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(StatusCodeEnum.USER_ALREADY_EXIST);
        }
        return new RestResponse<>(StatusCodeEnum.SUCCESS);
    }

    @PostMapping("/logout")
    public RestResponse<?> logout() {
        StpUtil.logout();
        return new RestResponse<>(StatusCodeEnum.SUCCESS);
    }

    @GetMapping("/islogin")
    public RestResponse<?> isLogin() {
        if (StpUtil.isLogin()) {
            return new RestResponse<>(StatusCodeEnum.SUCCESS, true);
        } else {
            return new RestResponse<>(StatusCodeEnum.SUCCESS, false);
        }
    }

    // @PostMapping("/ban")
    // @SaCheckRole("admin")
    // public RestResponse<?> ban(String username, int banDays) {
    //     User user = userService.findUserByName(username);
    //     if (user == null) {
    //         return new RestResponse<>(StatusCodeEnum.USER_NOT_EXIST);
    //     }
    //     // 从1970年1月1日00:00:00 GMT开始计算的毫秒数
    //     user.setBanEndTime(new Timestamp(System.currentTimeMillis() + (long) banDays * 24 * 60 * 60 * 1000));
    // }
}
