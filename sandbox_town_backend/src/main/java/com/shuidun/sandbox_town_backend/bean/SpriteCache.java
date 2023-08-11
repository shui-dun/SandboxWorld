package com.shuidun.sandbox_town_backend.bean;

import com.shuidun.sandbox_town_backend.enumeration.SpriteStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpriteCache {
    // 只有x和y会定期写入数据库，其他的都只在Java内存中
    private double x;

    private double y;

    private double vx;

    private double vy;

    // 上次更新坐标时间
    private long lastUpdateTime;

    // 上次与其他精灵交互时间
    private long lastInteractTime;

    SpriteStatus status;

    private String targetId;

    private Double targetX;

    private Double targetY;
}
