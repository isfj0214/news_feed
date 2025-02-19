package com.example.news_feed.friend.controller;

import com.example.news_feed.friend.dto.request.FriendRequestAcceptDto;
import com.example.news_feed.friend.dto.request.FriendRequestCancelDto;
import com.example.news_feed.friend.dto.request.FriendDeleteDto;
import com.example.news_feed.friend.dto.request.FriendRequestDto;
import com.example.news_feed.friend.dto.response.FriendRequestResponseDto;
import com.example.news_feed.friend.dto.response.FriendResponseDto;
import com.example.news_feed.friend.service.FriendService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;

    // 친구 신청하기
    @PostMapping()
    public ResponseEntity<FriendRequestResponseDto> request(@RequestBody @Valid FriendRequestDto friendRequestDto,
                                                            HttpServletRequest request){
        Long memberId = Long.parseLong((String)request.getAttribute("memberId"));
        FriendRequestResponseDto responseDto = friendService.request(friendRequestDto, memberId);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 친구 신청 취소하기
    @DeleteMapping("/request")
    public ResponseEntity<Void> cancel(@RequestBody @Valid FriendRequestCancelDto friendRequestCancelDto,
                                       HttpServletRequest request){
        Long memberId = Long.parseLong((String)request.getAttribute("memberId"));
        friendService.cancel(friendRequestCancelDto, memberId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 친구 신청 수락하기
    @PatchMapping()
    public ResponseEntity<Void> accept(@RequestBody @Valid FriendRequestAcceptDto acceptRequestDto,
                                       HttpServletRequest request){
        Long memberId = Long.parseLong((String)request.getAttribute("memberId"));
        friendService.accept(acceptRequestDto, memberId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 친구 삭제하기
    @DeleteMapping()
    public ResponseEntity<Void> delete(@RequestBody @Valid FriendDeleteDto friendDeleteDto,
                                       HttpServletRequest request){
        Long memberId = Long.parseLong((String)request.getAttribute("memberId"));
        friendService.delete(friendDeleteDto, memberId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 친구 신청을 건 목록 보기
    @GetMapping("/request_info")
    public ResponseEntity<List<FriendResponseDto>> getFriendRequestList(HttpServletRequest request){
        Long memberId = Long.parseLong((String)request.getAttribute("memberId"));
        List<FriendResponseDto> friendRequestList = friendService.getFriendRequestList(memberId);
        return new ResponseEntity<>(friendRequestList, HttpStatus.OK);
    }

    // 친구 신청을 받은 목록 보기
    @GetMapping("/received_info")
    public ResponseEntity<List<FriendResponseDto>> getFriendReceivedList(HttpServletRequest request){
        Long memberId = Long.parseLong((String)request.getAttribute("memberId"));
        List<FriendResponseDto> friendRequestList = friendService.getFriendReceivedList(memberId);
        return new ResponseEntity<>(friendRequestList, HttpStatus.OK);
    }

    // 친구인 사람 조회하기
    @GetMapping()
    public ResponseEntity<List<FriendResponseDto>> getFriendList(HttpServletRequest request){
        Long memberId = Long.parseLong((String)request.getAttribute("memberId"));
        List<FriendResponseDto> friendRequestList = friendService.getFriendList(memberId);
        return new ResponseEntity<>(friendRequestList, HttpStatus.OK);
    }
}
