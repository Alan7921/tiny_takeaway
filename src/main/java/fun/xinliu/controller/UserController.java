package fun.xinliu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import fun.xinliu.common.CustomException;
import fun.xinliu.common.Result;
import fun.xinliu.common.VerificationCode;
import fun.xinliu.entity.User;
import fun.xinliu.service.UserService;
import fun.xinliu.service.VerificationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    VerificationService verificationService;

    @PostMapping("/sendCode")
    public Result<String> sendCode(@RequestBody User user, HttpServletRequest request){

        if(Strings.isNotEmpty(user.getPhone())) {

            log.info("请求用户为: {}", user);
            VerificationCode vcode = verificationService.generateCode();
            log.info("发送的验证码为: {}", vcode.getCode());

            String text = "尊敬的用户:你好!欢迎您登陆使用本餐厅的外卖服务\n您的注册验证码为:" + vcode.getCode() + "(有效期为五分钟,请勿告知他人)";
            verificationService.sendMail(user.getPhone(),"登陆验证码", text);
            request.getSession().setAttribute(user.getPhone(), vcode);

            return Result.success("验证码发送成功");
        }

        return Result.error("验证码发送失败");
    }


    @PostMapping("/login")
    public Result<User> login(@RequestBody Map map, HttpServletRequest request){


        // 首先获取请求参数中带来的phone和code
        String phone = (String)map.get("phone");
        String receivedCode = (String)map.get("code");


        // 比对code和session中的code
        VerificationCode savedCode = (VerificationCode) request.getSession().getAttribute(phone);
        boolean valid = verificationService.isValid(receivedCode, savedCode);

        if (!valid) {
            return Result.error("验证码输入不正确，请重新尝试");
        }

        boolean isTimeOut = verificationService.isTimeOut(savedCode);

        if(isTimeOut){
            return Result.error("验证码已超时，请从新申请");
        }


        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper();
        lqw.eq( User::getPhone, phone);
        User user = userService.getOne(lqw);

        // 如果是新用户，保存入数据库
        if (user == null) {
            user = new User();
            user.setPhone(phone);
            userService.save(user);
        }

        request.getSession().setAttribute("user", user.getId());
        return Result.success(user);

    }

}
