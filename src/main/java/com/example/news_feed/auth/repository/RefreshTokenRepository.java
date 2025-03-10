package com.example.news_feed.auth.repository;

import com.example.news_feed.auth.entity.RefreshToken;
import com.example.news_feed.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByMember(Member member);

    Optional<RefreshToken> findByToken(String Token);

    @Modifying
    @Query("DELETE FROM RefreshToken r WHERE r.member.id = :memberId")
    void deleteByMemberId(@Param("memberId") Long memberId);
}
