package com.example.news_feed.auth.controller;

import com.example.news_feed.auth.dto.JwtTokenDto;
import com.example.news_feed.auth.dto.LoginRequestDto;
import com.example.news_feed.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/api/login")
    public ResponseEntity<JwtTokenDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto){
        return new ResponseEntity<>(authService.login(loginRequestDto),HttpStatus.OK);
    }

    @PostMapping("/api/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest httpServletRequest){

        String memberId = (String)httpServletRequest.getAttribute("memberId");
        authService.logout(Long.parseLong(memberId));

        Map<String, String> message = new HashMap<>();
        message.put("message", "로그아웃 되었습니다.");

        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
