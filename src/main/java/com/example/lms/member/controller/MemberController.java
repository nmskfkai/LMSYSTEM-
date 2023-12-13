package com.example.lms.member.controller;

import com.example.lms.admin.dto.MemberDto;
import com.example.lms.admin.model.OAuthToken;
import com.example.lms.course.dto.TakeCourseDto;
import com.example.lms.course.model.ServiceResult;
import com.example.lms.course.service.TakeCourseService;
import com.example.lms.member.model.MemberInput;
import com.example.lms.member.model.ResetPasswordInput;
import com.example.lms.member.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberService memberService;
    private final TakeCourseService takeCourseService;

    @RequestMapping("/member/login")
    public String login() {

        return "member/login";
    }

    @RequestMapping("/auth/kakao/callback")
    public @ResponseBody String kakaoCallback(String code) {

        RestTemplate rt = new RestTemplate();
        rt.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Accept", "application/json");
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "37c93ea0bb15b3614a2375487d27eea1");
        params.add("client_secret", "dnUYTjh6ZK2PNBbL60gWTCbImbavWaMq");
        params.add("redirect_uri", "http://localhost:8080/auth/kakao/callback");
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params, headers);

        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

//        ObjectMapper objectMapper = new ObjectMapper();
//        OAuthToken oauthToken = null;
//        try {
//            oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
//        } catch (JsonMappingException e) {
//            e.printStackTrace();
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("카카오 액세스 토큰" + oauthToken.getAccess_token());
//
//
//        RestTemplate rt2 = new RestTemplate();
//        HttpHeaders headers2 = new HttpHeaders();
//
//
//        headers2.add("Authorization", "Bearer" +oauthToken.getAccess_token());
//        headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//
//        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 =
//                new HttpEntity<>(headers2);
//
//        ResponseEntity<String> response2 = rt2.exchange(
//                "https://kapi.kakao.com/v2/user/me",
//                HttpMethod.POST,
//                kakaoProfileRequest2,
//                String.class
//        );
//        return response2.getBody();
        return "카카오 로그인 성공";
    }


    @GetMapping("/member/find-password")
    public String findPassword() {

        return "member/find_password";
    }

    @PostMapping("/member/find-password")
    public String findPasswordSubmit(Model model, ResetPasswordInput parameter) {

        boolean result = memberService.sendResetPassword(parameter);
        model.addAttribute("result", result);

        return "member/find_password_result";
    }


    @GetMapping("/member/register")
    public String register() {

        return "member/register";
    }

    @PostMapping("/member/register")
    public String registerSubmit(Model model, HttpServletRequest request
            , MemberInput parameter) {

        boolean result = memberService.register(parameter);
        model.addAttribute("result", result);

        return "member/register_complete";
    }

    // http://www.naver.com/news/list.do?id=123&key=124&text=쿼리
    // https://
    // 프로토콜://도메인(IP)/news/list.do?쿼리스트링(파라미터)
    // https://www.naver.com/cafe/detail.do?id=1111
    // https://www.naver.com/cafe/detail.do?id=2222


    @GetMapping("/member/email-auth")
    public String emailAuth(Model model, HttpServletRequest request) {

        String uuid = request.getParameter("id");
        System.out.println(uuid);

        boolean result = memberService.emailAuth(uuid);
        model.addAttribute("result", result);

        return "member/email_auth";
    }

    @GetMapping("/member/info")
    public String memberInfo(Model model, Principal principal) {

        String userId = principal.getName();
        MemberDto detail = memberService.detail(userId);
        model.addAttribute("detail", detail);



        return "member/info";
    }

    @PostMapping("/member/info")
    public String memberInfoSubmit(Model model
            , MemberInput parameter
            , Principal principal) {

        String userId = principal.getName();
        parameter.setUserId(userId);

        ServiceResult result = memberService.updateMember(parameter);
        if (!result.isResult()) {
            model.addAttribute("message", result.getMessage());
            return "common/error";
        }
        return "redirect:/member/info";
    }

    @GetMapping("/member/password")
    public String memberPassword(Model model, Principal principal) {

        String userId = principal.getName();
        MemberDto detail = memberService.detail(userId);

        model.addAttribute("detail", detail);

        return "member/password";
    }

    @PostMapping("/member/password")
    public String memberPasswordSubmit(Model model
            , MemberInput parameter
            , Principal principal) {

        String userId = principal.getName();
        parameter.setUserId(userId);

        ServiceResult result = memberService.updateMemberPassword(parameter);
        if (!result.isResult()) {
            model.addAttribute("message", result.getMessage());
            return "common/error";
        }

        return "redirect:/member/info";
    }


    @GetMapping("/member/takecourse")
    public String memberTakeCourse(Model model, Principal principal) {

        String userId = principal.getName();
        List<TakeCourseDto> list = takeCourseService.myCourse(userId);

        model.addAttribute("list", list);

        MemberDto detail = memberService.detail(userId);

        model.addAttribute("detail", detail);

        return "member/takecourse";
    }


    @GetMapping("/member/withdraw")
    public String memberWithdraw(Model model,Principal principal) {

        String userId = principal.getName();
        MemberDto detail = memberService.detail(userId);
        model.addAttribute("detail", detail);

        return "member/withdraw";
    }

    @PostMapping("/member/withdraw")
    public String memberWithdrawSubmit(Model model
            , MemberInput parameter
            , Principal principal) {

        String userId = principal.getName();

        ServiceResult result = memberService.withdraw(userId, parameter.getPassword());
        if (!result.isResult()) {
            model.addAttribute("message", result.getMessage());
            return "common/error";
        }

        return "redirect:/member/logout";
    }
}