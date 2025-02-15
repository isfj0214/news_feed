package com.example.news_feed.auth.repository;

import com.example.news_feed.auth.entity.RefreshToken;
import com.example.news_feed.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    boolean existsByMember(Member member);

    @Modifying
    @Query("DELETE FROM RefreshToken r WHERE r.member.memberId = :memberId")
    void deleteByMemberId(@Param("memberId") Long memberId);
}
