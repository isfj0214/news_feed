package com.example.news_feed.comment.service;


import com.example.news_feed.comment.dto.request.CommentRequestDto;
import com.example.news_feed.comment.dto.response.CommentResponseDto;
import com.example.news_feed.comment.entity.Comment;
import com.example.news_feed.comment.repository.CommentRepository;
import com.example.news_feed.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    //댓글 작성
    public CommentResponseDto save(CommentRequestDto requestDto) {
        Comment comment = new Comment(requestDto.getContents(),requestDto.getMember());
        commentRepository.save(comment);
        return new CommentResponseDto(comment.getContents(),comment.getMember());
    }

    //댓글 단건 조회(ById)
    public CommentResponseDto findById(Long id) {
        Comment comment = commentRepository.findByIdOrElseThrow(id);

        return new CommentResponseDto(comment.getContents(),comment.getMember());
    }

    //댓글 전체 조회
    public List<CommentResponseDto> findAll() {
        return commentRepository.findAll().stream().map(CommentResponseDto::toDto).toList();
    }

    //(본인이)좋아요 누른 댓글만 조회

    //댓글 수정
    @Transactional
    public void updateContents(Long id, String password, String contents)
    {
        Comment findComment = commentRepository.findByIdOrElseThrow(id);
        Member writer = findComment.getMember();

        if(!writer.getPassword().equals(password))
        {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"비밀번호가 일치하지 않습니다.");
        }
        findComment.setContents(contents);
    }


    //댓글 삭제
    public void deleteComment(Long id) {
        Comment findComment = commentRepository.findByIdOrElseThrow(id);

        commentRepository.delete(findComment);

    }


    //댓글 좋아요 누르기

    //댓글 좋아요 취소(삭제)
}
