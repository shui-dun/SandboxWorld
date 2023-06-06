package com.shuidun.sandbox_town_backend.service;

import com.shuidun.sandbox_town_backend.bean.Point;
import com.shuidun.sandbox_town_backend.bean.*;
import com.shuidun.sandbox_town_backend.mapper.BuildingMapper;
import com.shuidun.sandbox_town_backend.mapper.GameMapMapper;
import com.shuidun.sandbox_town_backend.utils.DataCompressor;
import com.shuidun.sandbox_town_backend.utils.PathUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 进行地图相关的操作，例如寻路算法
 */
@Slf4j
@Service
public class GameMapService {

    private final BuildingMapper buildingMapper;

    private final GameMapMapper gameMapMapper;

    private final int pixelsPerGrid = 30;

    // 地图ID
    private String mapId;

    /** 地图，用于寻路算法，0表示可以通过，非0表示障碍物ID的哈希值 */
    private int[][] map;

    private Random random = new Random();

    public GameMapService(BuildingMapper buildingMapper, GameMapMapper gameMapMapper, @Value("${mapId}") String mapId) {
        this.gameMapMapper = gameMapMapper;
        this.buildingMapper = buildingMapper;
        this.mapId = mapId;
        // 获得地图信息
        GameMap gameMap = gameMapMapper.getGameMapById(mapId);

        // 设置随机数种子
        random.setSeed(gameMap.getSeed());

        // 初始化地图
        map = new int[gameMap.getWidth() / pixelsPerGrid][gameMap.getHeight() / pixelsPerGrid];

        // 生成围墙
        generateMaze(0, 0, map.length, map[0].length);

        // 生成建筑
        placeAllBuildingsOnMap();
    }

    // 生成迷宫
    private void generateMaze(int x, int y, int w, int h) {
        if (w < 35 || h < 35) {
            return;
        }

        if (w < 70 || h < 70) {
            if (random.nextDouble() < 0.5) {
                return;
            }
        }

        int midX = x + w / 2;
        int midY = y + h / 2;

        // 画水平墙
        for (int i = x; i < x + w; i++) {
            map[i][midY] = 1;
        }

        // 拆除一部分，以保证可以通行
        int holeLen = 6 + random.nextInt(5);
        int beginX = x + random.nextInt(w / 2 - holeLen - 1);
        int endX = beginX + holeLen;
        for (int i = beginX; i < endX; i++) {
            map[i][midY] = 0;
        }

        holeLen = 6 + random.nextInt(5);
        beginX = x + w / 2 + random.nextInt(w / 2 - holeLen - 1);
        endX = beginX + holeLen;
        for (int i = beginX; i < endX; i++) {
            map[i][midY] = 0;
        }

        // 画竖直墙
        for (int i = y; i < y + h; i++) {
            map[midX][i] = 1;
        }

        holeLen = 6 + random.nextInt(5);
        int beginY = y + random.nextInt(h / 2 - holeLen - 1);
        int endY = beginY + holeLen;
        for (int i = beginY; i < endY; i++) {
            map[midX][i] = 0;
        }

        holeLen = 6 + random.nextInt(5);
        beginY = y + h / 2 + random.nextInt(h / 2 - holeLen - 1);
        endY = beginY + holeLen;
        for (int i = beginY; i < endY; i++) {
            map[midX][i] = 0;
        }

        // Recursively generate maze in each quadrant
        generateMaze(x, y, w / 2, h / 2);
        generateMaze(x + w / 2, y, w / 2, h / 2);
        generateMaze(x, y + h / 2, w / 2, h / 2);
        generateMaze(x + w / 2, y + h / 2, w / 2, h / 2);
    }

    // 将所有建筑物放置在地图上
    private void placeAllBuildingsOnMap() {
        // 建筑物的黑白图的字典
        var buildingTypes = this.buildingMapper.getAllBuildingTypes();
        Map<String, BufferedImage> buildingTypesImages = new ConcurrentHashMap<>();
        for (BuildingType buildingType : buildingTypes) {
            String buildingTypeId = buildingType.getId();
            String imagePath = buildingType.getImagePath();
            try {
                BufferedImage image = ImageIO.read(new ClassPathResource(imagePath).getInputStream());
                buildingTypesImages.put(buildingTypeId, image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 获取当前地图上的所有建筑物
        var buildings = buildingMapper.getAllBuildingsByMapId(mapId);

        // 将建筑放置在地图上
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[0].length; y++) {
                // 遍历每一个建筑物
                for (Building building : buildings) {
                    // 获取当前格的中心的像素坐标
                    int pixelX = x * pixelsPerGrid + pixelsPerGrid / 2;
                    int pixelY = y * pixelsPerGrid + pixelsPerGrid / 2;
                    // 获取建筑物的左上角的坐标
                    int buildingX = (int) building.getOriginX();
                    int buildingY = (int) building.getOriginY();
                    // 获取建筑物的宽高（暂时不知道宽和高写反了没有，因为现在的图片都是正方形的）
                    int buildingWidth = (int) building.getWidth();
                    int buildingHeight = (int) building.getHeight();
                    // 如果当前格子在建筑物的范围内
                    if (pixelX >= buildingX && pixelX < buildingX + buildingWidth &&
                            pixelY >= buildingY && pixelY < buildingY + buildingHeight) {
                        // 获取当前格子中心在建筑物黑白图中的坐标
                        int buildingPixelX = (int) ((double) (pixelX - buildingX) / buildingWidth * buildingTypesImages.get(building.getType()).getWidth());
                        int buildingPixelY = (int) ((double) (pixelY - buildingY) / buildingHeight * buildingTypesImages.get(building.getType()).getHeight());
                        // 获取当前格子中心的颜色
                        int color = buildingTypesImages.get(building.getType()).getRGB(buildingPixelX, buildingPixelY);
                        // 如果当前格子中心是黑色
                        if (color == Color.BLACK.getRGB()) {
                            // 将当前格子标记为不可通行
                            map[x][y] = building.getId().hashCode();
                        }
                        break;
                    }
                }
            }
        }
    }


    /** 寻路算法 */
    public List<Point> findPath(int x0, int y0, int x1, int y1, int itemWidth, int itemHeight, Integer destinationHashCode) {
        log.info("find path from ({}, {}) to ({}, {})", x0, y0, x1, y1);
        // 将物理坐标转换为地图坐标
        int startX = x0 / pixelsPerGrid;
        int startY = y0 / pixelsPerGrid;
        int endX = x1 / pixelsPerGrid;
        int endY = y1 / pixelsPerGrid;
        // 将物品宽高的像素转换为地图坐标
        int itemHalfWidth = (int) Math.ceil((double) itemWidth / pixelsPerGrid) / 2;
        int itemHalfHeight = (int) Math.ceil((double) itemHeight / pixelsPerGrid) / 2;
        log.info("item half width {}, item half height {}", itemHalfWidth, itemHalfHeight);
        // 调用寻路算法
        List<Point> path = PathUtils.findPath(map, startX, startY, endX, endY, itemHalfWidth, itemHalfHeight, destinationHashCode);
        // 判断是否为空
        if (path == null) {
            return null;
        }
        // 将地图坐标转换为物理坐标
        for (Point point : path) {
            point.setX(point.getX() * pixelsPerGrid + pixelsPerGrid / 2);
            point.setY(point.getY() * pixelsPerGrid + pixelsPerGrid / 2);
        }
        return path;
    }

    // 得到地图信息
    public GameMap getGameMap() {
        GameMap gameMap = gameMapMapper.getGameMapById(mapId);
        gameMap.setData(map);
        return gameMap;
    }
}
