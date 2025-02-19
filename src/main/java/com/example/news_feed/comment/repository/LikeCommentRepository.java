package com.example.news_feed.comment.repository;

import com.example.news_feed.comment.entity.Comment;
import com.example.news_feed.comment.entity.LikeComment;
import com.example.news_feed.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface LikeCommentRepository extends JpaRepository<LikeComment, Long> {
    // 특정 멤버가 특정 댓글에 좋아요를 눌렀는지 확인
    boolean existsByMemberAndComment(Member member, Comment comment);

    @Modifying
    @Transactional
    @Query("DELETE FROM LikeComment lc WHERE lc.comment.id IN :commentIds")
    void deleteByCommentIds(@Param("commentIds") List<Long> commentIds);

    // commentId에 해당하는 모든 LikeComment 삭제 (한 번의 쿼리 실행)
    @Modifying
    @Query("DELETE FROM LikeComment lc WHERE lc.comment.id = :commentId")
    void deleteByCommentId(@Param("commentId") Long commentId);

}
