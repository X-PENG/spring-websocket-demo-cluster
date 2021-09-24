package cn.monitor4all.springbootwebsocketdemo.mq;

import cn.monitor4all.springbootwebsocketdemo.mq.handler.MessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author xiezhipeng <xiezhipeng.peng@bytedance.com>
 * @Date 2021/09/24
 */
@Slf4j
@Component
public class ChatTopicConsumer {

    @Value("${rocket.consumer.broadcasting:false}")
    boolean broadcasting;

    @Autowired
    MessageHandler messageHandler;

    @PostConstruct
    public void constructConsumer() {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(RocketMqConfig.CONSUMER_GROUP);
        if (broadcasting) {
            log.info("启动广播消费模式");
            consumer.setMessageModel(MessageModel.BROADCASTING);
        }
        consumer.setNamesrvAddr(RocketMqConfig.NAMESRV_ADDR);
        try {
            consumer.subscribe(RocketMqConfig.CHAT_TOPIC, "*");
        } catch (MQClientException e) {
            log.error("ChatTopicConsumer subscribe fail, ", e);
        }

        consumer.registerMessageListener(messageHandler);

        try {
            consumer.start();
        } catch (MQClientException e) {
            log.error("ChatTopicConsumer start failed, ", e);
        }
    }
}
