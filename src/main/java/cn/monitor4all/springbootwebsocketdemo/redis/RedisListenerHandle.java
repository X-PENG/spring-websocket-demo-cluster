package cn.monitor4all.springbootwebsocketdemo.redis;

import cn.monitor4all.springbootwebsocketdemo.model.ChatMessage;
import cn.monitor4all.springbootwebsocketdemo.service.ChatService;
import cn.monitor4all.springbootwebsocketdemo.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Redis订阅频道处理类
 * @author yangzhendong01
 */
@Slf4j
@Component
public class RedisListenerHandle extends MessageListenerAdapter {

    @Value("${redis.channel.msgToAll}")
    private String msgToAll;

    @Value("${redis.channel.singleChat}")
    private String singleChat;

    @Value("${redis.channel.userStatus}")
    private String userStatus;

    @Value("${server.port}")
    private String serverPort;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ChatService chatService;

    /**
     * 收到监听消息
     * @param message
     * @param bytes
     */
    @Override
    public void onMessage(Message message, byte[] bytes) {
        byte[] body = message.getBody();
        byte[] channel = message.getChannel();
        String rawMsg;
        String topic;
        try {
            rawMsg = redisTemplate.getStringSerializer().deserialize(body);
            topic = redisTemplate.getStringSerializer().deserialize(channel);
            log.info("Received raw message from topic:" + topic + ", raw message content：" + rawMsg);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return;
        }


        ChatMessage chatMessage = JsonUtil.parseJsonToObj(rawMsg, ChatMessage.class);
        if (singleChat.equals(topic)){
            log.info("Send message to one user:" + rawMsg);
            if (chatMessage != null && ChatMessage.MessageType.SINGLE_CHAT.equals(chatMessage.getType()) && !StringUtils.isEmpty(chatMessage.getReceiver())) {
                chatService.pushMsgToUser(chatMessage);
            }
        } else if (msgToAll.equals(topic)) {
            log.info("Send message to all users:" + rawMsg);
            if (chatMessage != null && ChatMessage.MessageType.CHAT.equals(chatMessage.getType())) {
                chatService.pushMsg(chatMessage);
            }
        } else if (userStatus.equals(topic)) {
            if (chatMessage != null && ChatMessage.MessageType.JOIN.equals(chatMessage.getType())) {
                chatService.pushUserStatus(chatMessage);
            }
        }else {
            log.warn("No further operation with this topic!");
        }
    }
}
