package cn.monitor4all.springbootwebsocketdemo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/env")
public class EnvController {

    @Value("${server.port}")
    Integer port;

    @GetMapping("/port")
    public Integer port() {
        log.info("port = " + port);
        return port;
    }
}
