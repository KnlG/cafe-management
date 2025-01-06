package com.cafe.cafe_management.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.logging.log4j.message.SimpleMessage;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailUtils {

    private JavaMailSender emailSender;

    @Autowired
    public EmailUtils(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendSimpleMessage(String to, String subject, String text, List<String> list){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("konul.qurbanli1@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        if(list!=null && !list.isEmpty()){
            message.setCc(getCcArray(list));
        }
        emailSender.send(message);
    }

    private String[] getCcArray(List<String> ccList){
        String[] cc = new String[ccList.size()];
        for(int i=0; i<ccList.size(); i++){
            cc[i] = ccList.get(i);
        }
        return cc;
    }

    public void forgotPassword(String to, String subject, String password) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper messageHelper =  new MimeMessageHelper(message, true);
        messageHelper.setFrom("konul.qurbanli1@gmail.com");
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        String htmlMsg = "<p><b>Your Login details for Cafe Management System</b><br><b>Email: </b> " + to + " <br><b>Password: </b> " + password + "<br><a href=\"http://localhost:8081/user/login\">Click here to login</a></p>";
        messageHelper.setText(htmlMsg, true);
        emailSender.send(message);
    }
}
