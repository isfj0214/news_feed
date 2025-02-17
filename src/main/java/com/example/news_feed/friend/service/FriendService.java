package com.example.news_feed.friend.service;

import com.example.news_feed.friend.dto.request.FriendAcceptRequestDto;
import com.example.news_feed.friend.dto.request.FriendshipCancelDto;
import com.example.news_feed.friend.dto.request.FriendshipRequestDto;
import com.example.news_feed.friend.dto.response.FriendshipResponseDto;
import com.example.news_feed.friend.entity.Friend;
import com.example.news_feed.friend.repository.FriendRepository;
import com.example.news_feed.member.entity.Member;
import com.example.news_feed.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final MemberRepository memberRepository;


    @Transactional
    public FriendshipResponseDto request(FriendshipRequestDto friendshipRequestDto) {

        // 1. A가 B한테 친구신청을 보내서, from_id : a, to_id : b, is_friend : True를 insert
        Member member = memberRepository.findById(friendshipRequestDto.getToId()).orElseThrow(() -> new RuntimeException("회원 없음"));
        Friend friend = new Friend(friendshipRequestDto.getFromId(), member, true);
        Friend savedFriend = friendRepository.save(friend);

        // 2. 역방향 데이터 추가 from_id : b, to_id : a, is_friend : False를 insert
        Member reversedMember = memberRepository.findById(friendshipRequestDto.getFromId()).orElseThrow(() -> new RuntimeException("회원 없음"));
        Friend reversedFriend = new Friend(friendshipRequestDto.getToId(), reversedMember, false);
        friendRepository.save(reversedFriend);

        //TODO : 친구 재신청 방지 기능 추가
        return FriendshipResponseDto.builder()
                                    .fromId(savedFriend.getFromId())
                                    .toId(savedFriend.getMember().getId())
                                    .build();
    }

    @Transactional
    public void cancel(FriendshipCancelDto friendshipCancelDto) {
        memberRepository.findById(friendshipCancelDto.getFromId()).orElseThrow(() -> new RuntimeException("회원 없음"));
        memberRepository.findById(friendshipCancelDto.getToId()).orElseThrow(() -> new RuntimeException("회원 없음"));
        friendRepository.deleteFriendship(friendshipCancelDto.getToId(), friendshipCancelDto.getFromId());
    }


    @Transactional
    public void accept(FriendAcceptRequestDto acceptRequestDto) {
        // to_id 를 가져오기 위한 멤버 객체
//        Member toMember = memberRepository.findById(acceptRequestDto.getToId()).orElseThrow(() -> new RuntimeException("회원 없음"));
//        Friend friend = friendRepository.findByFromIdAndMember(acceptRequestDto.getFromId(), toMember);
//        friend.update(true);

        friendRepository.acceptFriendRequest(acceptRequestDto.getFromId(), acceptRequestDto.getToId());
    }


    public List<Long> getFriendRequestList(Long id) {
        List<Long> friendRequestList = friendRepository.getFriendRequestList(id);
        return friendRequestList;
    }

    public List<Long> getFriendReceivedList(Long id) {
        List<Long> friendReceivedtList = friendRepository.getFriendReceivedList(id);
        return friendReceivedtList;
    }

    public List<Long> getFriendList(Long id) {
        List<Long> friendList = friendRepository.getFriendList(id);
        return friendList;
    }
}
