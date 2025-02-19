package com.example.news_feed.post.repository;

import com.example.news_feed.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAll(Pageable pageable);

    @Modifying
    @Query("DELETE FROM Post p WHERE p.member.id = :memberId")
    void deleteByMemberId(@Param("memberId") Long memberId);
}
