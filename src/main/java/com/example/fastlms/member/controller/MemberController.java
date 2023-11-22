package com.example.fastlms.member.controller;

import com.example.fastlms.member.model.MemberInput;
import com.example.fastlms.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberService memberService;

    @RequestMapping("/member/login")
    public String login(){
        return "member/login";
    }
    @GetMapping("/member/register")
    public String register(){
        return "member/register";
    }
    @PostMapping("/member/register")
    // HttpServlet 값을 받아올 수 있는데 회원정보 controller 로 보냈을 때 정보가 request 객체 안에 다 들어감
    public String registerSubmit(Model model, HttpServletRequest request,
                                 MemberInput parameter){

        boolean result = memberService.register(parameter);
        model.addAttribute("result", result);

        return "member/register_complete";
    }

    @GetMapping("/member/email_auth")
    public String emailAuth(Model model, HttpServletRequest request){

        String uuid = request.getParameter("id");
        System.out.println(uuid);

        boolean result = memberService.emailAuth(uuid);
        model.addAttribute("result", result);

        return "email_auth";
    }


    @GetMapping("/member/info")
    public String memberInfo(){

        return "member/info";
    }

}
