package com.example.news_feed.friend.controller;

import com.example.news_feed.friend.dto.request.FriendAcceptRequestDto;
import com.example.news_feed.friend.dto.request.FriendshipCancelDto;
import com.example.news_feed.friend.dto.request.FriendshipRequestDto;
import com.example.news_feed.friend.dto.response.FriendshipResponseDto;
import com.example.news_feed.friend.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;

    // 친구 신청하기
    @PostMapping()
    public ResponseEntity<FriendshipResponseDto> request(@RequestBody FriendshipRequestDto friendshipRequestDto){
        FriendshipResponseDto responseDto = friendService.request(friendshipRequestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 친구 신청 취소하기
    @DeleteMapping()
    public ResponseEntity<Void> cancel(@RequestBody FriendshipCancelDto friendshipCancelDto){
        friendService.cancel(friendshipCancelDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 친구 신청 수락하기
    @PatchMapping()
    public ResponseEntity<Void> accept(@RequestBody FriendAcceptRequestDto acceptRequestDto){
        friendService.accept(acceptRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 친구 신청을 건 목록 보기
    @GetMapping("/request_info/{id}")
    public ResponseEntity<List<Long>> getFriendRequestList(@PathVariable Long id){
        List<Long> friendRequestList = friendService.getFriendRequestList(id);
        return new ResponseEntity<>(friendRequestList, HttpStatus.OK);
    }

    // 친구 신청을 받은 목록 보기
    @GetMapping("/received_info/{id}")
    public ResponseEntity<List<Long>> getFriendReceivedList(@PathVariable Long id){
        List<Long> friendRequestList = friendService.getFriendReceivedList(id);
        return new ResponseEntity<>(friendRequestList, HttpStatus.OK);
    }

    // 친구인 사람 조회하기
    @GetMapping("/{id}")
    public ResponseEntity<List<Long>> getFriendList(@PathVariable Long id){
        List<Long> friendRequestList = friendService.getFriendList(id);
        return new ResponseEntity<>(friendRequestList, HttpStatus.OK);
    }
}
