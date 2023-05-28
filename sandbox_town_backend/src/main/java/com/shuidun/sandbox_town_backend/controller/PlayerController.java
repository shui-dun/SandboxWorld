package com.shuidun.sandbox_town_backend.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.shuidun.sandbox_town_backend.bean.RestResponse;
import com.shuidun.sandbox_town_backend.enumeration.StatusCodeEnum;
import com.shuidun.sandbox_town_backend.service.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/player")
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @GetMapping("/list/{username}")
    public RestResponse<?> getPlayerByUsername(@PathVariable("username") String username) {
        return new RestResponse<>(StatusCodeEnum.SUCCESS, playerService.getPlayerInfoByUsername(username));
    }

    @GetMapping("/listMine")
    public RestResponse<?> getMyPlayerInfo() {
        String username = StpUtil.getLoginIdAsString();
        return new RestResponse<>(StatusCodeEnum.SUCCESS, playerService.getPlayerInfoByUsername(username));
    }
}
