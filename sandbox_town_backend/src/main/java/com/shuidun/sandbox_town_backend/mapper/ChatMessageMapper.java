package com.shuidun.sandbox_town_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shuidun.sandbox_town_backend.bean.ChatMessageDo;
import com.shuidun.sandbox_town_backend.enumeration.ChatMsgTypeEnum;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@Mapper
public interface ChatMessageMapper extends BaseMapper<ChatMessageDo> {

    /** 查询某个用户和某个好友的新于某个id的消息数量 */
    @Select("select count(*) from chat_message where source = #{user} and target = #{friend} and id > #{id}")
    Integer countNewerThanId(String user, String friend, String id);

    /** 删除指定时间前的消息 */
    @Delete("delete from chat_message where time < #{time}")
    void deleteMessageBefore(Date time);

    /** 加载两用户在某个消息前的指定长度的消息列表 */
    @Select("select * from chat_message where ((source = #{username} and target = #{friend}) or (source = #{friend} and target = #{username})) and id < #{messageId} order by id desc limit #{count}")
    List<ChatMessageDo> loadMessageBeforeId(String username, String friend, String messageId, Integer count);

    /** 加载两用户在某个消息前的、包含某个关键字的、指定长度的、指定类型的消息列表 **/
    @Select("""
            select * from chat_message
            where ((source = #{username} and target = #{friend}) or (source = #{friend} and target = #{username}))
            and id < #{messageId} and message like concat('%', #{keyword}, '%') and type = #{type}
            order by id desc limit #{count}
            """)
    List<ChatMessageDo> loadMessageBeforeIdWithKeyword(String username, String friend, String messageId, Integer count, String keyword, ChatMsgTypeEnum type);
}
