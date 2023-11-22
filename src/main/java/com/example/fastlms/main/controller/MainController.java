package com.example.fastlms.main.controller;

import com.example.fastlms.components.MailComponents;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
public class MainController {

    private final MailComponents mailComponents;
    @RequestMapping("/")
    public String index(){

//        String email = "leejimin00@naver.com";
//        String subject = "hi, jimin";
//        String text = "<p> hi </p> <p> jimin </p>";

//        mailComponents.sendMail(email, subject, text);
//        mailComponents.sendMailTest();
        return "index";
    }
}
