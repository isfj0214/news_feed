package com.example.news_feed.friend.controller;

import com.example.news_feed.friend.dto.request.FriendshipCancelDto;
import com.example.news_feed.friend.dto.request.FriendshipRequestDto;
import com.example.news_feed.friend.dto.response.FriendshipResponseDto;
import com.example.news_feed.friend.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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


}
