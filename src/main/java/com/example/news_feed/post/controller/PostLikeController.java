package com.example.news_feed.post.controller;

import com.example.news_feed.post.service.PostLikeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/likes")
public class PostLikeController {
    private final PostLikeService postLikeService;

    @PostMapping
    public ResponseEntity<Map<String, String>> createLike(@PathVariable("postId") Long postId,
                                                          HttpServletRequest request){
        Long memberId = Long.parseLong((String) request.getAttribute("memberId"));
        postLikeService.createLike(memberId, postId);

        Map<String, String> message = new HashMap<>();
        message.put("message", "좋아요 완료");

        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
