package com.shuidun.sandbox_town_backend.enumeration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/** 客户端向服务器发送的事件类型 */
@AllArgsConstructor
@Getter
public enum EventEnum {
    // 下线
    OFFLINE,
    // 告知坐标信息
    // data: {"id": "user_xixi", "x": 1, "y": 2, "vx": 3.0, "vy": 2.2}
    COORDINATE,
    // 想要移动到某个位置
    // data: {"x0": 1, "y0": 2, "x1": 3, "y1": 4, "dest_id" "store_1"}
    MOVE,
}
