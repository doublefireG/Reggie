package com.gy.reggie;

import com.gy.reggie.Utils.ValidateCodeUtils;
import com.gy.reggie.controller.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.server.Session;

@SpringBootTest
public class test {
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
}
