package com.example.lms.member.controller;

import com.example.lms.member.service.KakaoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
public class KakaoController {


    @Autowired
    KakaoService kakaoService;

    @GetMapping("/member/kakaoTerms")
    public String kakaoConnect() {

        StringBuffer url = new StringBuffer();
        url.append("https://kauth.kakao.com/oauth/authorize?");
        url.append("response_type=code");
        url.append("&client_id=" + "b9a046542d78ab2aa5321c87fe475bbc");
        url.append("&redirect_uri=http://localhost:8080/auth/kakao/callback");

        return "redirect:" + url.toString();
    }
    @RequestMapping("/auth/kakao/callback")
    public String kakaoLogin(@RequestParam("code")  String code, Model model , HttpSession session) throws Exception {

        String access_token = kakaoService.getToken(code);//code로 토큰 받음
        System.out.println("access_token : " + access_token);

        //토큰으로 사용자 정보 담은 list 가져오기
        ArrayList<Object> list = kakaoService.getUserInfo(access_token);

        //list 모델에 담아 view로 넘김
        model.addAttribute("list", list);

        return "/member/userInfo";
    }

}
