package com.shuidun.sandbox_town_backend.service;

import com.shuidun.sandbox_town_backend.bean.Sprite;
import com.shuidun.sandbox_town_backend.bean.SpriteType;
import com.shuidun.sandbox_town_backend.enumeration.StatusCodeEnum;
import com.shuidun.sandbox_town_backend.exception.BusinessException;
import com.shuidun.sandbox_town_backend.mapper.SpriteMapper;
import com.shuidun.sandbox_town_backend.mapper.SpriteTypeMapper;
import com.shuidun.sandbox_town_backend.utils.NameGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.shuidun.sandbox_town_backend.enumeration.Constants.EXP_PER_LEVEL;

@Slf4j
@Service
public class SpriteService {
    private final SpriteMapper spriteMapper;

    private final SpriteTypeMapper spriteTypeMapper;

    private final String mapId;

    public SpriteService(SpriteMapper spriteMapper, SpriteTypeMapper spriteTypeMapper, @Value("${mapId}") String mapId) {
        this.spriteMapper = spriteMapper;
        this.spriteTypeMapper = spriteTypeMapper;
        this.mapId = mapId;
    }

    public Sprite getSpriteInfoByID(String id) {
        Sprite sprite = spriteMapper.getSpriteById(id);
        if (sprite == null) {
            throw new BusinessException(StatusCodeEnum.USER_NOT_EXIST);
        }
        return sprite;
    }

    /**
     * 更新角色属性
     *
     * @param id        玩家用户名
     * @param attribute 属性名
     * @param value     属性值
     * @return 更新后的玩家信息
     */
    @Transactional
    public Sprite updateSpriteAttribute(String id, String attribute, int value) {
        try {
            spriteMapper.updateSpriteAttribute(id, attribute, value);
        } catch (BadSqlGrammarException e) {
            throw new BusinessException(StatusCodeEnum.ILLEGAL_ARGUMENT);
        }
        return getSpriteInfoByID(id);
    }

    /** 判断角色属性值是否在合理范围内（包含升级操作） */
    @Transactional
    public Sprite normalizeAndUpdatePlayer(Sprite sprite) {
        // 如果经验值足够升级，则升级
        if (sprite.getExp() >= EXP_PER_LEVEL) {
            sprite.setLevel(sprite.getLevel() + 1);
            sprite.setExp(sprite.getExp() - EXP_PER_LEVEL);
            // 更新玩家属性
            sprite.setMoney(sprite.getMoney() + 15);
            sprite.setHunger(sprite.getHunger() + 10);
            sprite.setHp(sprite.getHp() + 10);
            sprite.setAttack(sprite.getAttack() + 2);
            sprite.setDefense(sprite.getDefense() + 2);
            sprite.setSpeed(sprite.getSpeed() + 2);
        }
        // 判断属性是否在合理范围内
        if (sprite.getHunger() > 100) {
            sprite.setHunger(100);
        }
        if (sprite.getHunger() < 0) {
            sprite.setHunger(0);
        }
        if (sprite.getHp() > 100) {
            sprite.setHp(100);
        }
        if (sprite.getHp() < 0) {
            // 不能设置为0，因为0代表死亡
            sprite.setHp(1);
        }
        if (sprite.getAttack() < 0) {
            sprite.setAttack(0);
        }
        if (sprite.getDefense() < 0) {
            sprite.setDefense(0);
        }
        if (sprite.getSpeed() < 0) {
            sprite.setSpeed(0);
        }
        spriteMapper.updateById(sprite);
        return sprite;
    }

    // 得到某个地图上的所有角色
    public List<Sprite> getSpritesByMap(String map) {
        return spriteMapper.getSpritesByMap(map);
    }

    // 生成随机的指定类型的角色，并写入数据库
    public Sprite generateRandomSprite(String type, String owner, int x, int y) {
        Sprite sprite = new Sprite();
        SpriteType spriteType = spriteTypeMapper.selectById(type);
        sprite.setId(NameGenerator.generateItemName(type));
        sprite.setType(type);
        sprite.setOwner(owner);
        // 根据基础属性值和随机数随机生成角色的属性
        double scale = 0.8 + Math.random() * 0.4;
        sprite.setMoney((int) (spriteType.getBasicMoney() * scale));
        scale = 0.8 + Math.random() * 0.4;
        sprite.setExp((int) (spriteType.getBasicExp() * scale));
        scale = 0.8 + Math.random() * 0.4;
        sprite.setLevel((int) (spriteType.getBasicLevel() * scale));
        if (sprite.getLevel() < 1) {
            sprite.setLevel(1);
        }
        scale = 0.8 + Math.random() * 0.4;
        sprite.setHunger((int) (spriteType.getBasicHunger() * scale));
        if (sprite.getHunger() > 100) {
            sprite.setHunger(100);
        }
        scale = 0.8 + Math.random() * 0.4;
        sprite.setHp((int) (spriteType.getBasicHp() * scale));
        scale = 0.8 + Math.random() * 0.4;
        sprite.setAttack((int) (spriteType.getBasicAttack() * scale));
        scale = 0.8 + Math.random() * 0.4;
        sprite.setDefense((int) (spriteType.getBasicDefense() * scale));
        scale = 0.8 + Math.random() * 0.4;
        sprite.setSpeed((int) (spriteType.getBasicSpeed() * scale));
        sprite.setX(x);
        sprite.setY(y);
        // 宽度和高度使用相同的scale
        scale = 0.8 + Math.random() * 0.4;
        sprite.setWidth((int) (spriteType.getBasicWidth() * scale));
        sprite.setHeight((int) (spriteType.getBasicHeight() * scale));
        sprite.setMap(mapId);
        spriteMapper.insert(sprite);
        return sprite;
    }
}