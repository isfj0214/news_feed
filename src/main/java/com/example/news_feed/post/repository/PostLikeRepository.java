package com.example.news_feed.post.repository;

import com.example.news_feed.post.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByMember_IdAndPost_Id(Long memberId, Long postId);

    void deleteByMember_IdAndPost_Id(Long memberId, Long postId);
}
