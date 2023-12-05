package com.example.fastlms.member.controller;

import com.example.fastlms.member.model.MemberInput;
import com.example.fastlms.member.model.ResetPasswordInput;
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
    @GetMapping("/member/find-password")
    public String findPassword(){
        return "member/find_password";
    }
    @PostMapping("/member/find-password")
    public String findPasswordSubmit(Model model, ResetPasswordInput parameter) {

        boolean result = memberService.sendResetPassword(parameter);
        model.addAttribute("result", result);

        return "member/find_password_result";
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

    @GetMapping("/member/email-auth")
    public String emailAuth(Model model, HttpServletRequest request){

        String uuid = request.getParameter("id");
        System.out.println(uuid);

        boolean result = memberService.emailAuth(uuid);
        model.addAttribute("result", result);

        return "/member/email-auth";
    }


    @GetMapping("/member/info")
    public String memberInfo(){

        return "member/info";
    }

    @GetMapping("/member/reset/password")
    public String resetPassword(Model model, HttpServletRequest request){

        String uuid = request.getParameter("id");
        model.addAttribute("uuid", uuid);

        boolean result = memberService.checkResetPassword(uuid);
        model.addAttribute("result", result);

        return "member/reset_password";
    }
    @PostMapping("/member/reset/password")
    public String resetPasswordSubmit(Model model, ResetPasswordInput parameter){

        boolean result = false;
        try {
            result = memberService.resetPassword(parameter.getId(), parameter.getPassword());

        } catch (Exception ignored){
        }

        model.addAttribute("result", result);

        return "member/reset_password_result";
    }

}
