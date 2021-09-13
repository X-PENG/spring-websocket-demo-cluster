package cn.monitor4all.springbootwebsocketdemo.config;

import cn.monitor4all.springbootwebsocketdemo.controller.UserController;
import cn.monitor4all.springbootwebsocketdemo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Map;

/**
 * @author xiezhipeng <xiezhipeng.peng@bytedance.com>
 * @Date 2021/09/03
 */
@Slf4j
@Component
public class MyHandShakeHandler extends DefaultHandshakeHandler {
//    @Override
//    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
//        final HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
//        final HttpSession session = servletRequest.getSession();
//        final Object attribute = session.getAttribute(UserController.LOGIN_USER);
//        return new Principal() {
//            @Override
//            public String getName() {
//                return attribute.toString();
//            }
//        };
//    }


    @Autowired
    UserService userService;

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        log.info("MyHandShakeHandler.determineUser获取用户信息");
        //模拟调用其他服务
        String userName = userService.getUserName(((ServletServerHttpRequest) request).getServletRequest().getSession());
        log.info("userName = {}", userName);
        return new Principal() {
            @Override
            public String getName() {
                return userName;
            }
        };
    }
}
