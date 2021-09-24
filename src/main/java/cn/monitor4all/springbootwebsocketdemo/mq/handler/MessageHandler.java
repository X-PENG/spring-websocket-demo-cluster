package cn.monitor4all.springbootwebsocketdemo.mq.handler;

import cn.monitor4all.springbootwebsocketdemo.model.ChatMessage;
import cn.monitor4all.springbootwebsocketdemo.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author xiezhipeng <xiezhipeng.peng@bytedance.com>
 * @Date 2021/09/24
 */
@Slf4j
@Component
public class MessageHandler implements MessageListenerConcurrently {
    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        for(int i = 0; i < msgs.size(); i++){
            MessageExt msg = msgs.get(i);
            String payload = new String(msg.getBody());
            log.info("消费消息： topic={}, tag={}, msg={}", msg.getTopic(), msg.getTags(), payload);
            //处理消息
            handle(JsonUtil.parseJsonToObj(payload, ChatMessage.class));
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    public void handle(ChatMessage chatMessage) {
        Assert.isTrue(!StringUtils.isEmpty(chatMessage.getSender()), "sender is empty");
        ChatMessage.MessageType type = chatMessage.getType();
        if (type != null) {
            switch (type) {
                case JOIN:
                case CHAT:
                case LEAVE:
                    pushMsgToAll(chatMessage);
                    break;
                case SINGLE_CHAT:
                    pushMsgToUser(chatMessage);
                    break;
            }
        }
    }

    private void pushMsgToAll(ChatMessage chatMessage) {
        log.info("pushMsg:" + chatMessage.toString());
        simpMessageSendingOperations.convertAndSend("/topic/public", chatMessage);
    }

    private void pushMsgToUser(ChatMessage chatMessage) {
        log.info("pushMsgToUser:" + chatMessage.toString());
        Assert.isTrue(!StringUtils.isEmpty(chatMessage.getReceiver()), "receiver is empty");
        simpMessageSendingOperations.convertAndSendToUser(chatMessage.getReceiver(), "/unique/chat", chatMessage);
    }
}
