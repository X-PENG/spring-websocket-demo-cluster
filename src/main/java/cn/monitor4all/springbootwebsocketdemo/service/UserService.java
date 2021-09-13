package cn.monitor4all.springbootwebsocketdemo.service;

import cn.monitor4all.springbootwebsocketdemo.controller.UserController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

/**
 * 为了模拟调用其他服务
 * @author xiezhipeng <xiezhipeng.peng@bytedance.com>
 * @Date 2021/09/13
 */
@Slf4j
@Service
public class UserService {

    //延迟3秒，模拟微服务调用的网络延迟。延迟过长会导致websocket连接失败，todo 如何配置解决？
    private static final int DELAY = 1000;

    public String getUserName(HttpSession session) {
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("UserService.getUserName");
        return session.getAttribute(UserController.LOGIN_USER).toString();
    }

    public boolean checkUserLogin(HttpSession session) {
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("UserService.checkUserLogin");
        Object loginUser = session.getAttribute(UserController.LOGIN_USER);
        return loginUser != null;
    }
}
