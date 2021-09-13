package cn.monitor4all.springbootwebsocketdemo.config;

import cn.monitor4all.springbootwebsocketdemo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * @author xiezhipeng <xiezhipeng.peng@bytedance.com>
 * @Date 2021/09/13
 */
@Slf4j
@Component
public class UserAuthenticationHandshakeInterceptor implements HandshakeInterceptor {

    @Autowired
    UserService userService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        log.info("UserAuthenticationHandshakeInterceptor.beforeHandshake用户登陆认证");
        //模拟调用其他服务
        boolean checkUserLogin = userService.checkUserLogin(((ServletServerHttpRequest) request).getServletRequest().getSession());
        log.info("checkUserLogin = {}", checkUserLogin);
        return checkUserLogin;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
