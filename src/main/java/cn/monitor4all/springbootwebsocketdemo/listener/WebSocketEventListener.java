package cn.monitor4all.springbootwebsocketdemo.listener;

import cn.monitor4all.springbootwebsocketdemo.model.ChatMessage;
import cn.monitor4all.springbootwebsocketdemo.service.ChatServiceForProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.net.Inet4Address;
import java.net.InetAddress;

@Slf4j
@Component
public class WebSocketEventListener {

    @Value("${server.port}")
    private String serverPort;

    @Value("${redis.set.onlineUsers}")
    private String onlineUsers;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ChatServiceForProducer chatServiceForProducer;

    public static String sesionId;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        InetAddress localHost;
        try {
            localHost = Inet4Address.getLocalHost();
            SimpMessageHeaderAccessor simpMessageHeaderAccessor = SimpMessageHeaderAccessor.wrap(event.getMessage());
            System.out.println(simpMessageHeaderAccessor.getSessionId());
            sesionId = simpMessageHeaderAccessor.getSessionId();
            log.info("Received a new web socket connection from:" + localHost.getHostAddress() + ":" + serverPort);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        //todo 为什么这里可以get到
        String username = (String) headerAccessor.getSessionAttributes().get("username");

        if(username != null) {
            log.info("User Disconnected : " + username);
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setType(ChatMessage.MessageType.LEAVE);
            chatMessage.setSender(username);
            try {
                redisTemplate.opsForSet().remove(onlineUsers, username);
                chatServiceForProducer.pushMsg(chatMessage);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
