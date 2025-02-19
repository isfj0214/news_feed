package com.example.news_feed.post.controller;

import com.example.news_feed.auth.JwtUtil;
import com.example.news_feed.post.dto.request.PostCreateRequestDto;
import com.example.news_feed.post.dto.response.PostResponseDto;
import com.example.news_feed.post.dto.response.PostCreateResponseDto;
import com.example.news_feed.post.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")

public class PostController {

    private final PostService postService;
    private final JwtUtil jwtUtil;

    //게시물 생성
    @PostMapping("/posts")
    public PostCreateResponseDto createPost(@RequestBody PostCreateRequestDto dto, HttpServletRequest request) {
        Long memberId = Long.parseLong((String) request.getAttribute("memberId"));
        return postService.createPost(dto, memberId);
    }

    //모든 게시물 조회
    @GetMapping("/posts")
    public List<PostResponseDto> findAll() {
        return postService.findAll();
    }

    //자기가 쓴 게시물만 조회니까 List로
    @GetMapping("/posts/{postId}")
    public PostResponseDto findById(@PathVariable Long postId) {
        return postService.findById(postId);
    }

    //API 명세서에는 {postId}없던데,
    // 어떤 게시물인지 알아야 수정할 수 있지 않나?
    @PatchMapping("/posts/{postId}")
    public PostResponseDto update(
            @PathVariable Long postId,
            @RequestBody PostCreateRequestDto dto,
            HttpServletRequest request
            ) {
        Long memberId = Long.parseLong((String) request.getAttribute("memberId"));
        return postService.update(postId, memberId, dto);

    }
}
