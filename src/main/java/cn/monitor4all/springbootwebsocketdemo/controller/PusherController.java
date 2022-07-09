package cn.monitor4all.springbootwebsocketdemo.controller;

import cn.monitor4all.springbootwebsocketdemo.listener.WebSocketEventListener;
import cn.monitor4all.springbootwebsocketdemo.model.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/push")
public class PusherController {


    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    SimpMessageSendingOperations simpMessageSendingOperations;

    @GetMapping("/broadcast")
    public String push1() {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender("SYSTEM");
        chatMessage.setType(ChatMessage.MessageType.CHAT);
        chatMessage.setContent("系统测试【广播】消息【" + (int)(Math.random() * 100 + 1) + "】");
        simpMessagingTemplate.convertAndSend("/topic/public", chatMessage);
        return String.valueOf(simpMessageSendingOperations == simpMessagingTemplate);//true
    }

    @GetMapping("/unique/{username}")
    public String push2(@PathVariable String username) {
        log.info("psuh to " + username);
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender("SYSTEM");
        chatMessage.setType(ChatMessage.MessageType.CHAT);
        chatMessage.setContent("系统测试【单聊】消息【" + (int)(Math.random() * 100 + 1) + "】");
        simpMessageSendingOperations.convertAndSendToUser(username, "/unique/chat", chatMessage);
        return String.valueOf(simpMessageSendingOperations == simpMessagingTemplate);//true
    }

    /**
     * 测试自己生成用户唯一的user destination
     * @param username
     * @return
     */
    @GetMapping("/t3/{username}")
    public String push3(@PathVariable String username) {
        log.info("psuh to " + username);
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender("SYSTEM");
        chatMessage.setType(ChatMessage.MessageType.CHAT);
        chatMessage.setContent("system test push 【USER】 message 【" + (int)(Math.random() * 100 + 1) + "】");
//        simpMessageSendingOperations.convertAndSendToUser(username, "/unique/chat", chatMessage);
//        simpMessageSendingOperations.convertAndSend("/unique/" + username, chatMessage);
        System.out.println("---->  /unique/chat-user" + WebSocketEventListener.sesionId);
        simpMessageSendingOperations.convertAndSend("/unique/chat-user" + WebSocketEventListener.sesionId, chatMessage);
        return String.valueOf(simpMessageSendingOperations == simpMessagingTemplate);//true
    }
}
