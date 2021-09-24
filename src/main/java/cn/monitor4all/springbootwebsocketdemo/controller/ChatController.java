package cn.monitor4all.springbootwebsocketdemo.controller;

import cn.monitor4all.springbootwebsocketdemo.model.ChatMessage;
import cn.monitor4all.springbootwebsocketdemo.service.ChatServiceForProducer;
import cn.monitor4all.springbootwebsocketdemo.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

@Slf4j
@Controller
public class ChatController {

    @Value("${redis.set.onlineUsers}")
    private String onlineUsers;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    ChatServiceForProducer chatServiceForProducer;

    @MessageMapping("/chat.send")
    public void send(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        if (ChatMessage.MessageType.JOIN.equals(chatMessage.getType()) && !StringUtils.isEmpty(chatMessage.getSender())) {
            log.info("user added");
            //headerAccessor实际类型：StompHeaderAccessor，每次连接得到的对象都不一样
            //todo 这个原理是啥
            headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
            redisTemplate.opsForSet().add(onlineUsers, chatMessage.getSender());
        }
        chatServiceForProducer.pushMsg(chatMessage);
    }
}
