package com.example.news_feed.post.repository;

import com.example.news_feed.post.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByMember_IdAndPost_Id(Long memberId, Long postId);

    void deleteByMember_IdAndPost_Id(Long memberId, Long postId);

    List<PostLike> findAllByPost_Id(Long postId);

    @Modifying
    @Query("DELETE FROM PostLike pl WHERE pl.member.id = :memberId")
    void deleteByMemberId(@Param("memberId") Long memberId);

    @Modifying
    @Query("DELETE FROM PostLike pl WHERE pl.post.id = :postId")
    void deleteByPostId(@Param("postId") Long postId);

}
