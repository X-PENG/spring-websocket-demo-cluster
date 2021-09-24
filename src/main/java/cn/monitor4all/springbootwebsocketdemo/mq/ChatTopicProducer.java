package cn.monitor4all.springbootwebsocketdemo.mq;

import cn.monitor4all.springbootwebsocketdemo.model.ChatMessage;
import cn.monitor4all.springbootwebsocketdemo.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author xiezhipeng <xiezhipeng.peng@bytedance.com>
 * @Date 2021/09/24
 */
@Slf4j
@Component
public class ChatTopicProducer {

    DefaultMQProducer producer;

    @PostConstruct
    public void constructProducer() {
        log.info("constructProducer");
        producer = new DefaultMQProducer(RocketMqConfig.PRODUCER_GROUP);
        producer.setNamesrvAddr(RocketMqConfig.NAMESRV_ADDR);
        try {
            producer.start();
        } catch (MQClientException e) {
            log.error("ChatTopicProducer start fail, ", e);
        }
    }

    public SendResult produceMessage(ChatMessage payload) {
        Message msg = new Message();
        msg.setTopic(RocketMqConfig.CHAT_TOPIC);
        msg.setBody(JsonUtil.parseObjToJson(payload).getBytes());

        try {
            return producer.send(msg);
        } catch (Exception e) {
            log.warn("produceMessage occur exception, ", e);
        }
        return null;
    }
}
