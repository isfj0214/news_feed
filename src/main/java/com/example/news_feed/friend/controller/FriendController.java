package com.example.news_feed.friend.controller;

import com.example.news_feed.friend.dto.request.FriendshipRequestDto;
import com.example.news_feed.friend.dto.response.FriendshipResponseDto;
import com.example.news_feed.friend.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;

    @PostMapping()
    public ResponseEntity<FriendshipResponseDto> request(@RequestBody FriendshipRequestDto friendshipRequestDto){
           FriendshipResponseDto responseDto = friendService.request(friendshipRequestDto);
           return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

}
