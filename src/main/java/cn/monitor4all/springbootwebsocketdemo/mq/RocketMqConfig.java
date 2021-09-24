package cn.monitor4all.springbootwebsocketdemo.mq;

/**
 * @author xiezhipeng <xiezhipeng.peng@bytedance.com>
 * @Date 2021/09/24
 */
public class RocketMqConfig {
    public static final String CHAT_TOPIC = "testChat";
    public static final String NAMESRV_ADDR = "127.0.0.1:9876";
    public static final String CONSUMER_GROUP = "chat-consumer-1";
    public static final String PRODUCER_GROUP = "chat-producer-1";
}
