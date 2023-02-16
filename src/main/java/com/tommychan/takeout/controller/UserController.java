package com.tommychan.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tommychan.takeout.common.R;
import com.tommychan.takeout.dto.UserDto;
import com.tommychan.takeout.entity.User;
import com.tommychan.takeout.service.UserService;
import com.tommychan.takeout.utils.SMSUtils;
import com.tommychan.takeout.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {

        //获取手机号
        String phone = user.getPhone();

        if (StringUtils.isNotEmpty(phone)) {
            //生成随机验证码
            String validateCode = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info(validateCode);
            //保存在session
            session.setAttribute(phone, validateCode);
            //发送消息
            SMSUtils.sendMessage("阿里云短信测试", "SMS_154950909", phone, validateCode);
            return R.success("验证码发送成功！");
        }
        return R.error("验证码发送失败");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody UserDto userDto, HttpSession session) {

        String validateCode = userDto.getCode();
        String phone = userDto.getPhone();

        //验证
        if (validateCode.equals(session.getAttribute(phone))) {
            //若已存在用户 则登陆成功
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getPhone, phone);
            User user = userService.getOne(wrapper);

            //不存在则创建新用户
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user", user.getId());
            return R.success(user);
        }
        return R.error("登录失败");
    }

    @PostMapping("/loginout")
    public R<String> loginOut(HttpSession session) {
        session.removeAttribute("userPhone");
        return R.success("退出成功");
    }

}
