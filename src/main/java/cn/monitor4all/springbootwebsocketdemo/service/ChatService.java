package cn.monitor4all.springbootwebsocketdemo.service;

import cn.monitor4all.springbootwebsocketdemo.model.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ChatService {

    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    /**
     * 推送聊天消息
     * @param chatMessage
     */
    public void pushMsg(@Payload ChatMessage chatMessage) {
        log.info("Send msg by simpMessageSendingOperations:" + chatMessage.toString());
        simpMessageSendingOperations.convertAndSend("/topic/public", chatMessage);
    }

    public void pushUserStatus(@Payload ChatMessage chatMessage) {
        log.info("Alert user online by simpMessageSendingOperations:" + chatMessage.toString());
        simpMessageSendingOperations.convertAndSend("/topic/public", chatMessage);
    }
}
