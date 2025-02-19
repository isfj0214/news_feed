package com.example.news_feed.comment.repository;

import com.example.news_feed.comment.entity.Comment;
import com.example.news_feed.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Modifying
    @Query("DELETE FROM Comment c WHERE c.post.id IN (SELECT p.id FROM Post p WHERE p.member.id = :memberId)")
    void deleteByMemberId(@Param("memberId") Long memberId);
}
