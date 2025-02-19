package com.example.news_feed.comment.repository;

import com.example.news_feed.comment.entity.Comment;
import com.example.news_feed.comment.entity.LikeComment;
import com.example.news_feed.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeCommentRepository extends JpaRepository<LikeComment, Long> {
    // 특정 멤버가 특정 댓글에 좋아요를 눌렀는지 확인
    boolean existsByMemberAndComment(Member member, Comment comment);
}
