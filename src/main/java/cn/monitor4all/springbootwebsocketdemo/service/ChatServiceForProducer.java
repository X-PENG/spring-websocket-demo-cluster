package cn.monitor4all.springbootwebsocketdemo.service;

import cn.monitor4all.springbootwebsocketdemo.model.ChatMessage;
import cn.monitor4all.springbootwebsocketdemo.mq.ChatTopicProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ChatServiceForProducer {

    @Autowired
    ChatTopicProducer chatTopicProducer;

    public void pushMsg(ChatMessage chatMessage) {
        chatTopicProducer.produceMessage(chatMessage);
    }
}
