package com.example.news_feed.post.controller;

import com.example.news_feed.auth.JwtUtil;
import com.example.news_feed.post.dto.request.PostCreateRequestDto;
import com.example.news_feed.post.dto.response.PostResponseDto;
import com.example.news_feed.post.dto.response.PostCreateResponseDto;
import com.example.news_feed.post.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")

public class PostController {

    private final PostService postService;
    private final JwtUtil jwtUtil;

    //게시글 생성
    @PostMapping("/posts")
    public PostCreateResponseDto createPost(@RequestBody PostCreateRequestDto dto, HttpServletRequest request) {
        Long memberId = Long.parseLong((String) request.getAttribute("memberId"));
        return postService.createPost(dto, memberId);
    }

    //모든 게시글 조회
    @GetMapping("/posts")
    public List<PostResponseDto> findAll(@PageableDefault(size = 10) Pageable pageable) {
        return postService.findAll(pageable);
    }

    //한 게시글 조회
    @GetMapping("/posts/{postId}")
    public PostResponseDto findById(@PathVariable Long postId) {
        return postService.findById(postId);
    }

    //작성자만 수정 가능
    @PatchMapping("/posts/{postId}")
    public PostResponseDto update(
            @PathVariable Long postId,
            @RequestBody PostCreateRequestDto dto,
            HttpServletRequest request
    ) {
        Long memberId = Long.parseLong((String) request.getAttribute("memberId"));
        return postService.update(postId, memberId, dto);
    }

    //작성자만 삭제 가능
    @DeleteMapping("/posts/{postId}")
    public void delete(
            @PathVariable Long postId,
            HttpServletRequest request
    ) {
        Long memberId = Long.parseLong((String) request.getAttribute("memberId"));
        postService.deleteById(postId, memberId);
    }
}
