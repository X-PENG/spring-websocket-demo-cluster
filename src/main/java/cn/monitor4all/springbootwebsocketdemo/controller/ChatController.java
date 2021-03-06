package cn.monitor4all.springbootwebsocketdemo.controller;

import cn.monitor4all.springbootwebsocketdemo.model.ChatMessage;
import cn.monitor4all.springbootwebsocketdemo.service.ChatService;
import cn.monitor4all.springbootwebsocketdemo.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class ChatController {

    @Value("${redis.channel.msgToAll}")
    private String msgToAll;

    @Value("${redis.channel.singleChat}")
    private String singleChat;

    @Value("${redis.set.onlineUsers}")
    private String onlineUsers;

    @Value("${redis.channel.userStatus}")
    private String userStatus;

    @Autowired
    private ChatService chatService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @MessageMapping("/chat.sendAll")
    public void sendAll(@Payload ChatMessage chatMessage) {
        try {
            //给群聊channel发布聊天消息
            redisTemplate.convertAndSend(msgToAll, JsonUtil.parseObjToJson(chatMessage));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @MessageMapping("/chat.sendToUser")
    public void sendToUser(@Payload ChatMessage chatMessage) {
        try {
            //给单聊channel发布聊天消息
            redisTemplate.convertAndSend(singleChat, JsonUtil.parseObjToJson(chatMessage));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @MessageMapping("/chat.addUser")
    public void addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        log.info("User added in Chatroom:" + chatMessage.getSender());
        try {
            //headerAccessor实际类型：StompHeaderAccessor，每次连接得到的对象都不一样
            headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
            redisTemplate.opsForSet().add(onlineUsers, chatMessage.getSender());
            //给channel发布用户上线消息
            redisTemplate.convertAndSend(userStatus, JsonUtil.parseObjToJson(chatMessage));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
