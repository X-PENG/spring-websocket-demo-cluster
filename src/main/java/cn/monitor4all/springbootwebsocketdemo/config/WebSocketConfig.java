package cn.monitor4all.springbootwebsocketdemo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.config.SimpleBrokerRegistration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.*;


@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private static long HEART_BEAT = 3000;

    @Autowired
    MyHandShakeHandler myHandShakeHandler;

    @Autowired
    UserAuthenticationHandshakeInterceptor userAuthenticationHandshakeInterceptor;

    /**
     * 是否开启心跳
     */
    @Value("${peng.websocket.stomp.heartbeat}")
    boolean enableHeaderBeat;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").addInterceptors(userAuthenticationHandshakeInterceptor).setHandshakeHandler(myHandShakeHandler).withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");

        // Enables a simple in-memory broker
        SimpleBrokerRegistration simpleBrokerRegistration = registry.enableSimpleBroker("/topic", "/unique");

        if (enableHeaderBeat) {
            enableHeartbeat(simpleBrokerRegistration);
        }

        //   Use this for enabling a Full featured broker like RabbitMQ
        /*
        registry.enableStompBrokerRelay("/topic")
                .setRelayHost("localhost")
                .setRelayPort(61613)
                .setClientLogin("guest")
                .setClientPasscode("guest");
        */
    }

    /**
     * 开启心跳机制
     * @param simpleBrokerRegistration
     */
    public void enableHeartbeat(SimpleBrokerRegistration simpleBrokerRegistration) {
        log.info("开启心跳");
        //配置心跳任务
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(1);
        scheduler.setThreadNamePrefix("wss-heartbeat-thread-");
        scheduler.initialize();
        simpleBrokerRegistration.setHeartbeatValue(new long[]{HEART_BEAT, HEART_BEAT}).setTaskScheduler(scheduler);
    }
}
