package com.example.news_feed.friend.repository;

import com.example.news_feed.friend.dto.response.FriendResponseDto;
import com.example.news_feed.friend.entity.Friend;
import com.example.news_feed.member.entity.Member;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Friend f" +
            " WHERE (f.fromId = :fromId AND f.member.id = :toId) " +
            "OR (f.fromId = :toId AND f.member.id = :fromId)")
    void deleteFriendRequest(@Param("fromId") Long fromId, @Param("toId") Long toId);

    @Modifying
    @Query("UPDATE Friend f " +
            "SET f.isFriend = TRUE " +
            "WHERE f.fromId = :fromId AND f.member.id = :toId")
    void acceptFriendRequest(@Param("fromId") Long fromId, @Param("toId") Long toId);

    @Query("SELECT new com.example.news_feed.friend.dto.response.FriendResponseDto(f.member.id, f.member.name) " +
            "FROM Friend f " +
            "INNER JOIN Friend r " +
            "ON f.fromId = r.member.id AND r.fromId = f.member.id" +
            " WHERE f.fromId = :memberId AND f.isFriend = True" +
            " AND r.member.id = :memberId AND r.isFriend = False")
    List<FriendResponseDto> getFriendRequestList(@Param("memberId") Long memberId);

    @Query("SELECT f.fromId " +
            "FROM Friend f " +
            "INNER JOIN Friend r " +
            "ON f.fromId = r.member.id AND r.fromId = f.member.id" +
            " WHERE f.member.id = :memberId AND f.isFriend = True" +
            " AND r.fromId = :memberId AND r.isFriend = False")
    List<Long> getFriendReceivedList(@Param("memberId") Long memberId);

    @Query("SELECT new com.example.news_feed.friend.dto.response.FriendResponseDto(f.member.id, f.member.name) " +
            "FROM Friend f " +
            "INNER JOIN Friend r " +
            "ON f.fromId = r.member.id AND r.fromId = f.member.id" +
            " WHERE f.fromId = :memberId AND f.isFriend = True" +
            " AND r.member.id = :memberId AND r.isFriend = True")
    List<FriendResponseDto> getFriendList(@Param("memberId")Long id);

    boolean existsByFromIdAndMemberId(Long fromId, Long toId);

    @Query("SELECT COUNT(f) " +
            "FROM Friend f " +
            "INNER JOIN Friend r " +
            "ON f.fromId = r.member.id AND r.fromId = f.member.id" +
            " WHERE f.fromId = :fromId AND f.member.id = :toId AND f.isFriend = True" +
            " AND r.fromId = :toId AND r.member.id = :fromId AND r.isFriend = True")
    Long isFriend(@Param("fromId")Long fromId, @Param("toId")Long toId);

    @Modifying
    @Query("DELETE FROM Friend f WHERE f.member.id = :memberId")
    void deleteByMemberId(@Param("memberId") Long memberId);
}
