package fun.xinliu.service.impl;

import fun.xinliu.common.VerificationCode;
import fun.xinliu.service.VerificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * The Email service.
 *
 */
@Service
public class VerificationServiceImpl implements VerificationService {
    @Resource
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String from;


    /**
     * 发送纯文本邮件.
     *
     * @param to      目标email 地址
     * @param subject 邮件主题
     * @param text    纯文本内容
     */
    public void sendMail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    @Override
    public VerificationCode generateCode() {
        String[] beforeShuffle = new String[]{"2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F",
                "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a",
                "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
                "w", "x", "y", "z"};
        List<String> list = Arrays.asList(beforeShuffle);//将数组转换为集合
        Collections.shuffle(list);  //打乱集合顺序
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s); //将集合转化为字符串
        }

        VerificationCode vcode = new VerificationCode();
        vcode.setCode(sb.substring(0, 6));

        Date now = new Date();
        Date validUntil = new Date(now.getTime() + 300000);

        vcode.setDate(validUntil);
        return vcode;
    }

    @Override
    public  boolean isValid(String receivedCode, VerificationCode savedCode) {
        // 如果收到的code为空或者内容和我们存储的验证码不相等
        if(receivedCode == null || !(receivedCode.equals(savedCode.getCode()))){
            return false;
        }
        return true;
    }
    @Override
    public  boolean isTimeOut(VerificationCode savedCode) {
        Date now = new Date();
        if(now.after(savedCode.getDate())) {
            return true;
        }
        return false;
    }


}