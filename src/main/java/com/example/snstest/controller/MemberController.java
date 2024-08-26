package com.example.snstest.controller;

import com.example.snstest.service.AuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final AuthService authService;

    @GetMapping("/member/login")
    public String login() {
        return "login";
    }
    @GetMapping("/auth/kakao/login")
    public String kakaoLogin() {
        System.out.println("kakao login");
        String location = authService.getkakaoLoginURI();
        return "redirect:" + location;
    }

    @GetMapping("/auth/kakao/callback")
    public String kakaoCallback(String code, HttpSession session){
        System.out.println("code = " + code);

        Long memberId = authService.getMemberIdByKakaoInfo(code);
        authService.getMemberIdByKakaoInfo(code);

        session.setAttribute("memberId", memberId);

        return "redirect:/";
    }
}
