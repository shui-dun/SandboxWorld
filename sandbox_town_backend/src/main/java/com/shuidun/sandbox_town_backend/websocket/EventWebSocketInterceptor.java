package com.shuidun.sandbox_town_backend.websocket;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

/**
 * 握手阶段的拦截器，对连接进行过滤，可以对连接前和连接后自定义处理
 */
@Service
@Slf4j
public class EventWebSocketInterceptor extends HttpSessionHandshakeInterceptor {
    /**
     * 握手前
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        log.info("call beforeHandshake");
        // 先要通过shiro判断用户是否登录
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            log.info("用户未登录");
            return false;
        }
        attributes.put("userName", "player1");
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception ex) {
        log.info("call afterHandshake");
        super.afterHandshake(request, response, wsHandler, ex);
    }

}