package cn.monitor4all.springbootwebsocketdemo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    public static final String LOGIN_USER = "login_user";

    @GetMapping("/login/{username}")
    public String login(@PathVariable String username, HttpSession session) {
        log.info("login:" + username);
        session.setAttribute(LOGIN_USER, username);
        return "success";
    }
}
