package com.example.news_feed.friend.service;

import com.example.news_feed.friend.dto.request.FriendRequestAcceptDto;
import com.example.news_feed.friend.dto.request.FriendRequestCancelDto;
import com.example.news_feed.friend.dto.request.FriendDeleteDto;
import com.example.news_feed.friend.dto.request.FriendRequestDto;
import com.example.news_feed.friend.dto.response.FriendRequestResponseDto;
import com.example.news_feed.friend.dto.response.FriendResponseDto;
import com.example.news_feed.friend.entity.Friend;
import com.example.news_feed.friend.repository.FriendRepository;
import com.example.news_feed.member.entity.Member;
import com.example.news_feed.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public FriendRequestResponseDto request(FriendRequestDto friendRequestDto) {
        isAlreadyFriend(friendRequestDto);
        isSelfFriendRequest(friendRequestDto);
        isFriendRequestExists(friendRequestDto);

        // 1. 정방향 데이터 추가 : A가 B한테 친구신청을 보내서, from_id : a, to_id : b, is_friend : True를 insert
        Member member = memberRepository.findById(friendRequestDto.getToId()).orElseThrow(() -> new RuntimeException("회원 없음"));
        Friend friend = new Friend(friendRequestDto.getFromId(), member, true);
        Friend savedFriend = friendRepository.save(friend);

        // 2. 역방향 데이터 추가 : from_id : b, to_id : a, is_friend : False를 insert
        Member reversedMember = memberRepository.findById(friendRequestDto.getFromId()).orElseThrow(() -> new RuntimeException("회원 없음"));
        Friend reversedFriend = new Friend(friendRequestDto.getToId(), reversedMember, false);
        friendRepository.save(reversedFriend);

        return FriendRequestResponseDto.builder()
                                    .fromId(savedFriend.getFromId())
                                    .toId(savedFriend.getMember().getMemberId())
                                    .build();
    }

    @Transactional
    public void cancel(FriendRequestCancelDto friendRequestCancelDto) {
        memberRepository.findById(friendRequestCancelDto.getFromId()).orElseThrow(() -> new RuntimeException("회원 없음"));
        memberRepository.findById(friendRequestCancelDto.getToId()).orElseThrow(() -> new RuntimeException("회원 없음"));
        isFriendRequestExists(friendRequestCancelDto);
        friendRepository.deleteFriendship(friendRequestCancelDto.getToId(), friendRequestCancelDto.getFromId());
    }

    @Transactional
    public void accept(FriendRequestAcceptDto acceptRequestDto) {
        isFriendReceivedExists(acceptRequestDto);
        friendRepository.acceptFriendRequest(acceptRequestDto.getFromId(), acceptRequestDto.getToId());
    }

    @Transactional
    public void delete(FriendDeleteDto friendDeleteDto) {
        isFriends(friendDeleteDto);
        friendRepository.deleteFriendship(friendDeleteDto.getToId(), friendDeleteDto.getFromId());
    }

    @Transactional(readOnly = true)
    public List<FriendResponseDto> getFriendRequestList(Long id) {
        List<FriendResponseDto> friendRequestList = friendRepository.getFriendRequestList(id);
        return friendRequestList;
    }

    @Transactional(readOnly = true)
    public List<FriendResponseDto> getFriendReceivedList(Long id) {
        List<Long> friendReceivedtList = friendRepository.getFriendReceivedList(id);
        List<Member> members = memberRepository.findAllById(friendReceivedtList);

        return FriendResponseDto.membersBuildToDto(members);
    }

    @Transactional(readOnly = true)
    public List<FriendResponseDto> getFriendList(Long id) {
        List<FriendResponseDto> friendList = friendRepository.getFriendList(id);
        return friendList;
    }

    /*
     * 검증 메서드 - 찬구 신청시 검증
     */
    private void isFriendRequestExists(FriendRequestDto friendRequestDto) {
        /*
         * 1. A가 B한테 친구 요청을 보냈는데, B가 A한테 친구요청을 보내려고 할 때 안된다고 하는 경우
         * 2. A가 B한테 친구 요청을 보냈는데, 한번 더 동일한 친구 요청(A가 B한테 친구 요청)을 보내는 경우에 안된다고 하는 경우
         */
        if(friendRepository.existsByFromIdAndMemberId(friendRequestDto.getFromId(), friendRequestDto.getToId())){
            throw new RuntimeException("이미 존재하는 친구 요청이 있습니다.");
        }
    }

    private static void isSelfFriendRequest(FriendRequestDto friendRequestDto) {
        // 3. 자기 자신한테 친구 요청을 보낼 때(A가 A에게 친구 요청), 안된다고 하는 경우
        if (friendRequestDto.getFromId().equals(friendRequestDto.getToId())){
            throw new RuntimeException("자기 자신에게는 친구 요청을 보낼 수 없습니다.");
        }
    }

    private void isAlreadyFriend(FriendRequestDto friendRequestDto) {
        //4. 이미 친구인데 친구 요청을 보낼 때, 안된다고 하는 경우
        Long count = friendRepository.isFriend(friendRequestDto.getFromId(), friendRequestDto.getToId());
        if (count == 1){
            throw new RuntimeException("이미 친구로 등록된 친구입니다.");
        }
    }

    /*
     * 검증 메서드 - 찬구 요청 취소시 검증
     */
    private void isFriendRequestExists(FriendRequestCancelDto friendRequestCancelDto) {
        // 존재하지 않는 친구 신청(내가 보낸 요청)을 취소하려고 하는 경우
        List<FriendResponseDto> friendRequestList = friendRepository.getFriendRequestList(friendRequestCancelDto.getFromId());
        List<Long> friendRequestIdList = friendRequestList.stream()
                .map(FriendResponseDto::getId)
                .collect(Collectors.toList());
        if (!friendRequestIdList.contains(friendRequestCancelDto.getToId())){
            throw new RuntimeException("존재하지 않는 친구 신청입니다.");
        }
    }

    /*
     * 검증 메서드 - 찬구 요청 수락시 검증
     */
    private void isFriendReceivedExists(FriendRequestAcceptDto acceptRequestDto) {
        // 존재하지 않는 친구 신청(내가 받은 요청)을 수락하려고 하는 경우
        List<Long> friendReceivedtList = friendRepository.getFriendReceivedList(acceptRequestDto.getFromId());
        if (!friendReceivedtList.contains(acceptRequestDto.getToId())){
            throw new RuntimeException("존재하지 않는 친구 신청입니다.");
        }
    }

    /*
     * 검증 메서드 - 찬구 삭제시 검증
     */
    private void isFriends(FriendDeleteDto friendDeleteDto) {
        // 친구가 아닌 사람을 삭제하려고 하는 경우
        List<FriendResponseDto> friendList = friendRepository.getFriendList(friendDeleteDto.getFromId());
        List<Long> friendIdList = friendList.stream()
                .map(FriendResponseDto::getId)
                .collect(Collectors.toList());
        if (!friendIdList.contains(friendDeleteDto.getToId())){
            throw new RuntimeException("존재하지 않는 친구는 삭제할 수 없습니다.");
        }
    }
}
