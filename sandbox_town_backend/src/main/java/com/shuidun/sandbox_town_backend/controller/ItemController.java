package com.shuidun.sandbox_town_backend.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.shuidun.sandbox_town_backend.bean.ItemDetailBo;
import com.shuidun.sandbox_town_backend.bean.ItemTypeDetailBo;
import com.shuidun.sandbox_town_backend.bean.RestResponseVo;
import com.shuidun.sandbox_town_backend.enumeration.ItemPositionEnum;
import com.shuidun.sandbox_town_backend.enumeration.ItemTypeEnum;
import com.shuidun.sandbox_town_backend.enumeration.StatusCodeEnum;
import com.shuidun.sandbox_town_backend.service.ItemService;
import com.shuidun.sandbox_town_backend.service.ItemTypeService;
import com.shuidun.sandbox_town_backend.service.SpriteService;
import com.shuidun.sandbox_town_backend.websocket.WSMessageSender;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@RestController
@RequestMapping("/item")
@Slf4j
public class ItemController {

    private final ItemService itemService;

    private final ItemTypeService itemTypeService;

    private final SpriteService spriteService;

    public ItemController(ItemService itemService, ItemTypeService itemTypeService, SpriteService spriteService) {
        this.itemService = itemService;
        this.itemTypeService = itemTypeService;
        this.spriteService = spriteService;
    }

    @Operation(summary = "获取当前登陆玩家的所有物品信息")
    @GetMapping("/listMyItems")
    public RestResponseVo<List<ItemDetailBo>> listMyItems() {
        return new RestResponseVo<>(StatusCodeEnum.SUCCESS,
                itemService.listByOwner(StpUtil.getLoginIdAsString()).stream()
                        .map(itemService::getItemDetailById)
                        .toList());
    }

    @Operation(summary = "获取当前登陆玩家的背包中的所有物品信息")
    @GetMapping("/listMyItemsInBackpack")
    public RestResponseVo<List<ItemDetailBo>> listMyItemsInBackpack() {
        return new RestResponseVo<>(StatusCodeEnum.SUCCESS,
                itemService.listByOwnerAndPositions(StpUtil.getLoginIdAsString(),
                        List.of(ItemPositionEnum.BACKPACK)));
    }

    @Operation(summary = "获取当前登陆玩家的装备栏中的所有物品信息")
    @GetMapping("/listMyItemsInEquipment")
    public RestResponseVo<List<ItemDetailBo>> listMyItemsInEquipment() {
        return new RestResponseVo<>(StatusCodeEnum.SUCCESS,
                itemService.listItemsInEquipmentByOwner(StpUtil.getLoginIdAsString()));
    }

    @Operation(summary = "获取当前登陆玩家的物品栏中的所有物品信息")
    @GetMapping("/listMyItemsInItemBar")
    public RestResponseVo<List<ItemDetailBo>> listMyItemsInItemBar() {
        return new RestResponseVo<>(StatusCodeEnum.SUCCESS,
                itemService.listItemsInItemBarByOwner(StpUtil.getLoginIdAsString()));
    }


    @Operation(summary = "使用物品")
    @PostMapping("/use")
    public RestResponseVo<Void> use(@NotNull @RequestParam String itemId) {
        // 之所以这里要以websocket而非http的方式发送消息，
        // 是因为http的方式发送消息，只能发送给当前请求的用户，
        // 而websocket的方式发送消息，可以发送给需要该消息的所有用户
        WSMessageSender.addResponses(spriteService.useItem(StpUtil.getLoginIdAsString(), itemId));
        return new RestResponseVo<>(StatusCodeEnum.SUCCESS);
    }

    @Operation(summary = "显示某个物品的详细信息")
    @GetMapping("/itemDetail")
    public RestResponseVo<ItemDetailBo> detail(@NotNull @RequestParam String itemId) {
        return new RestResponseVo<>(StatusCodeEnum.SUCCESS,
                itemService.getItemDetailById(itemId));
    }

    @Operation(summary = "显示某个物品类型的详细信息")
    @GetMapping("/itemTypeDetail")
    public RestResponseVo<ItemTypeDetailBo> detailByItemType(@NotNull @RequestParam ItemTypeEnum itemType) {
        return new RestResponseVo<>(StatusCodeEnum.SUCCESS,
                itemTypeService.getItemTypeById(itemType));
    }

    @Operation(summary = "手持物品")
    @PostMapping("/hold")
    public RestResponseVo<Void> hold(@NotNull @RequestParam String itemId) {
        WSMessageSender.addResponses(itemService.changeItemPosition(StpUtil.getLoginIdAsString(), itemId, ItemPositionEnum.HANDHELD));
        return new RestResponseVo<>(StatusCodeEnum.SUCCESS);
    }

    @Operation(summary = "放入物品栏")
    @PostMapping("/putInItemBar")
    public RestResponseVo<Void> putInItemBar(@NotNull @RequestParam String itemId) {
        WSMessageSender.addResponses(itemService.changeItemPosition(StpUtil.getLoginIdAsString(), itemId, ItemPositionEnum.ITEMBAR));
        return new RestResponseVo<>(StatusCodeEnum.SUCCESS);
    }

    @Operation(summary = "装备物品")
    @PostMapping("/equip")
    public RestResponseVo<Void> equip(@NotNull @RequestParam String itemId) {
        WSMessageSender.addResponses(itemService.equip(StpUtil.getLoginIdAsString(), itemId));
        return new RestResponseVo<>(StatusCodeEnum.SUCCESS);
    }

    @Operation(summary = "放入背包")
    @PostMapping("/putInBackpack")
    public RestResponseVo<Void> putInBackpack(@NotNull @RequestParam String itemId) {
        WSMessageSender.addResponses(itemService.changeItemPosition(StpUtil.getLoginIdAsString(), itemId, ItemPositionEnum.BACKPACK));
        return new RestResponseVo<>(StatusCodeEnum.SUCCESS);
    }
}
