package com.shuidun.sandbox_town_backend.controller;

import com.shuidun.sandbox_town_backend.bean.RestResponse;
import com.shuidun.sandbox_town_backend.enumeration.StatusCodeEnum;
import com.shuidun.sandbox_town_backend.service.MapService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/map")
public class MapController {

    private final MapService mapService;

    public MapController(MapService mapService) {
        this.mapService = mapService;
    }


    /** 得到地图的信息，包含地图尺寸、建筑列表等信息 */
    @GetMapping("/getMapInfo")
    public RestResponse<?> getMapInfo() {
        return new RestResponse<>(StatusCodeEnum.SUCCESS, mapService.getMapInfo());
    }
}