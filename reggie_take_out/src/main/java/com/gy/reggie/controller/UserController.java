package com.gy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gy.reggie.Utils.SMSUtils;
import com.gy.reggie.Utils.ValidateCodeUtils;
import com.gy.reggie.common.R;
import com.gy.reggie.entity.User;
import com.gy.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendmsg(@RequestBody User user,HttpSession httpSession) throws Exception {
        String phone = user.getPhone();
        if(StringUtils.isNotEmpty(phone)){
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            SMSUtils.sendMessage("阿里云短信测试","SMS_154950909",phone,code);
            httpSession.setAttribute("phone",phone);
            httpSession.setAttribute("code",code);

            System.out.println(httpSession.getAttribute("phone"));
            System.out.println(httpSession.getAttribute("code"));
            return R.success("发送短信成功");
        }

        return R.error("发送短信失败");
    }
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map,HttpSession httpSession){
        log.info(map.toString());
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();

        Object sessisonCode = httpSession.getAttribute("code");

        if (sessisonCode!=null && sessisonCode.equals(code)){
            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userLambdaQueryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(userLambdaQueryWrapper);
            if(user == null){
                user = new User();
                user.setPhone(phone);
                userService.save(user);

            }
            httpSession.setAttribute("user",user.getId());
            return R.success(user);
        }
        return R.error("登陆失败");

    }
}
