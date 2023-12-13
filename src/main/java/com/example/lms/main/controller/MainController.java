package com.example.lms.main.controller;

import com.example.lms.admin.dto.MemberDto;
import com.example.lms.components.MailComponents;
import com.example.lms.member.service.impl.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import java.security.Principal;

@RequiredArgsConstructor
@Controller
public class MainController {

    private final MailComponents mailComponents;
    private final MemberServiceImpl memberService;

    @RequestMapping("/")
    public String index(){

//        String email = "leejimin00@naver.com";
//        String subject = "hi, jimin";
//        String text = "<p> hi </p> <p> jimin </p>";

//        mailComponents.sendMail(email, subject, text);
//        mailComponents.sendMailTest();

        return "index";
    }
    @GetMapping
    public  String main(Model model, Principal principal){
        String userId = principal.getName();
        MemberDto detail = memberService.detail(userId);
        model.addAttribute("detail", detail);
        return "index";
    }

    @RequestMapping("/error/denied")
    public String errorDenied(){
        return "error/denied";
    }
}

