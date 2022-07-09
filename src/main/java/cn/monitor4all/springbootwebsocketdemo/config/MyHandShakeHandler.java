package cn.monitor4all.springbootwebsocketdemo.config;

import cn.monitor4all.springbootwebsocketdemo.controller.UserController;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Map;

@Component
public class MyHandShakeHandler extends DefaultHandshakeHandler {
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        final HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
        final HttpSession session = servletRequest.getSession();
        final Object attribute = session.getAttribute(UserController.LOGIN_USER);
        return new Principal() {
            @Override
            public String getName() {
                return attribute.toString();
            }
        };
    }
}
