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
        //TODO : 친구 재신청 방지 기능 추가
        /*
         * 4. 이미 친구인데 친구 요청을 보낼 때, 안된다고 하는 경우
         */
        Long count = friendRepository.isFriend(friendshipRequestDto.getFromId(), friendshipRequestDto.getToId());
        if (count == 1){
            throw new RuntimeException("이미 친구로 등록된 친구입니다.");
        }

        /*
         * 3. 자기 자신한테 친구 요청을 보낼 때, 안된다고 하는 경우
         */
        if (friendshipRequestDto.getFromId().equals(friendshipRequestDto.getToId())){
            throw new RuntimeException("자기 자신에게는 친구 요청을 보낼 수 없습니다.");
        }

        /*
         * 1. 1이 2한테 친구 요청을 보냈는데, 2가 1한테 친구요청을 보내려고 할 때 안된다고 하는 경우
         * 2. 1이 2한테 친구 요청을 보냈는데, 한번 더 동일한 친구 요청을 보내는 경우에 안된다고 하는 경우
         */
        if(friendRepository.existsByFromIdAndMemberId(friendshipRequestDto.getFromId(), friendshipRequestDto.getToId())){
            throw new RuntimeException("이미 존재하는 친구 요청이 있습니다.");
        }


        /*
         * 친구 요청 생성
         */
        // 1. A가 B한테 친구신청을 보내서, from_id : a, to_id : b, is_friend : True를 insert
        Member member = memberRepository.findById(friendshipRequestDto.getToId()).orElseThrow(() -> new RuntimeException("회원 없음"));
        Friend friend = new Friend(friendshipRequestDto.getFromId(), member, true);
        Friend savedFriend = friendRepository.save(friend);

        // 2. 역방향 데이터 추가 from_id : b, to_id : a, is_friend : False를 insert
        Member reversedMember = memberRepository.findById(friendshipRequestDto.getFromId()).orElseThrow(() -> new RuntimeException("회원 없음"));
        Friend reversedFriend = new Friend(friendshipRequestDto.getToId(), reversedMember, false);
        friendRepository.save(reversedFriend);

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
        friendRepository.acceptFriendRequest(acceptRequestDto.getFromId(), acceptRequestDto.getToId());
    }


    @Transactional(readOnly = true)
    public List<Long> getFriendRequestList(Long id) {
        List<Long> friendRequestList = friendRepository.getFriendRequestList(id);
        return friendRequestList;
    }

    @Transactional(readOnly = true)
    public List<Long> getFriendReceivedList(Long id) {
        List<Long> friendReceivedtList = friendRepository.getFriendReceivedList(id);
        return friendReceivedtList;
    }

    @Transactional(readOnly = true)
    public List<Long> getFriendList(Long id) {
        List<Long> friendList = friendRepository.getFriendList(id);
        return friendList;
    }
}
