package com.gy.reggie;

import com.gy.reggie.Utils.ValidateCodeUtils;
import com.gy.reggie.controller.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@SpringBootTest
public class test {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void t1(){
        String password = "123456";
        byte[] bytes = password.getBytes();
        for (int i = 0; i < bytes.length; i++) {
            System.out.println(bytes[i]);
        }
    }
    @Test
    public void t2(){
        Integer code = ValidateCodeUtils.generateValidateCode(4);
        System.out.println(code);
    }
    @Test
    public void t3(){
        UserController userController;
        Session session = new Session();
        Session.Cookie cookie = session.getCookie();
    }
    @Test
    public void t4(){
        redisTemplate.opsForValue().set("key1","aa",10l, TimeUnit.SECONDS);
    }
}
