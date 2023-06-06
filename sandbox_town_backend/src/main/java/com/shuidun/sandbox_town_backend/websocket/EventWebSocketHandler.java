package com.shuidun.sandbox_town_backend.websocket;

import com.alibaba.fastjson2.JSONObject;
import com.shuidun.sandbox_town_backend.bean.EventMessage;
import com.shuidun.sandbox_town_backend.enumeration.EventEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * websocket消息处理器
 */
@Service
@Slf4j
public class EventWebSocketHandler extends TextWebSocketHandler {

    private final EventHandler eventHandler;

    public EventWebSocketHandler(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    /**
     * 建立连接后
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userName = (String) session.getAttributes().get("userName");
        // 如果用户已经存在，删除之前的session
        if (WSManager.usernameSession.containsKey(userName)) {
            WebSocketSession webSocketSession = WSManager.usernameSession.get(userName);
            if (webSocketSession.isOpen()) {
                webSocketSession.close();
            }
        }
        // 保存用户session
        WSManager.usernameSession.put(userName, session);
        log.info("call afterConnectionEstablished");
    }

    /**
     * 任何原因导致WebSocket连接中断时，会自动调用afterConnectionClosed方法
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("call afterConnectionClosed");
        String userName = (String) session.getAttributes().get("userName");
        WSManager.usernameSession.remove(userName, session);
        // 发出下线事件
        EventMessage eventMessage = new EventMessage(EventEnum.OFFLINE, userName, null);
        eventHandler.handle(eventMessage);
        super.afterConnectionClosed(session, status);
    }

    /**
     * 收到消息时进行的操作（转发给目标用户）
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        String messagePayload = message.getPayload();
        if ("".equals(messagePayload)) {
            return;
        }
        EventMessage eventMessage = JSONObject.parseObject(messagePayload, EventMessage.class);
        eventMessage.setInitiator((String) session.getAttributes().get("userName"));
        // 交给事件处理器处理
        eventHandler.handle(eventMessage);
    }

}
