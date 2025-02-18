package com.example.news_feed.comment.controller;

import com.example.news_feed.comment.dto.request.CommentRequestDto;
import com.example.news_feed.comment.dto.request.CommentUpdateRequestDto;
import com.example.news_feed.comment.dto.response.CommentResponseDto;
import com.example.news_feed.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    //댓글 작성
    @PostMapping
    public ResponseEntity<CommentResponseDto> commentSave(@RequestBody CommentRequestDto requestDto)
    {
        CommentResponseDto commentSaveResponseDto = commentService.creteComment(requestDto);
        return new ResponseEntity<>(commentSaveResponseDto, HttpStatus.CREATED);
    }

    //댓글 단건 조회(ById)
    @GetMapping("/{id}")
    public ResponseEntity<CommentResponseDto> commentFindById(@PathVariable Long id){
        CommentResponseDto commentResponseDto = commentService.findById(id);
        return new ResponseEntity<>(commentResponseDto,HttpStatus.OK);
    }

    //(본인이) 쓴 댓글만 조회
    //@GetMapping
    //public ResponseEntity<List<CommentResponseDto>> commentFindByUser(@PathVariable)


    //댓글 전체 조회
    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> commentFindAll(){
        List<CommentResponseDto> scheduleResponseDtoList = commentService.findAll();

        return new ResponseEntity<>(scheduleResponseDtoList, HttpStatus.OK);
    }

    //댓글 수정
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateTitle(
            @PathVariable Long id,
            @RequestBody CommentUpdateRequestDto requestDto
    ){
        commentService.updateContents(id,requestDto.getPassword(), requestDto.getContents());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //댓글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id){
        commentService.deleteComment(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
