package com.shuidun.sandbox_town_backend.mapper;

import com.shuidun.sandbox_town_backend.bean.Character;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CharacterMapper {
    // 根据角色id获取角色信息（包含character_type表中的角色描述信息）
    @Select("SELECT * FROM `character` INNER JOIN character_type " +
            "ON `character`.type = character_type.type " +
            "WHERE id = #{id}")
    Character getCharacterById(@Param("id") String id);


    @Update("UPDATE `character` SET ${attribute} = #{value} WHERE id = #{id}")
    void updateCharacterAttribute(@Param("id") String id, @Param("attribute") String attribute, @Param("value") int value);

    // 创建角色
    @Insert("INSERT INTO `character` (id, type, owner, money, exp, level, hunger, hp, attack, defense, speed, X, Y, map) " +
            "VALUES (#{character.id}, #{character.type}, #{character.owner}, #{character.money}, #{character.exp}, " +
            "#{character.level}, #{character.hunger}, #{character.hp}, #{character.attack}, #{character.defense}, " +
            "#{character.speed}, #{character.X}, #{character.Y}, #{character.map})")
    void createCharacter(@Param("character") Character character);

    // 更新角色
    @Update("UPDATE `character` SET type = #{character.type}, owner = #{character.owner}, money = #{character.money}, " +
            "exp = #{character.exp}, level = #{character.level}, hunger = #{character.hunger}, hp = #{character.hp}, " +
            "attack = #{character.attack}, defense = #{character.defense}, speed = #{character.speed}, X = #{character.X}, " +
            "Y = #{character.Y}, map = #{character.map} WHERE id = #{character.id}")
    void updateCharacter(@Param("character") Character character);

    // 得到某个地图上的所有角色
    @Select("SELECT * FROM `character` WHERE map = #{map}")
    List<Character> getCharactersByMap(@Param("map") String map);
}
