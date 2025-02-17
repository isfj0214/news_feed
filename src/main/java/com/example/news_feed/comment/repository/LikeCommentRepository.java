package com.example.news_feed.comment.repository;

import com.example.news_feed.comment.entity.LikeComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeCommentRepository extends JpaRepository<LikeComment, Long> {
}
