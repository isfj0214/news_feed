package com.example.news_feed.friend.service;

import com.example.news_feed.common.error.ErrorCode;
import com.example.news_feed.common.error.exception.Exception400;
import com.example.news_feed.common.error.exception.Exception404;
import com.example.news_feed.common.error.exception.Exception409;
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
    public FriendRequestResponseDto request(FriendRequestDto friendRequestDto, Long memberId) {
        isAlreadyFriend(memberId, friendRequestDto);
        isSelfFriendRequest(memberId, friendRequestDto);
        isFriendRequestExists(memberId, friendRequestDto);

        // 1. 정방향 데이터 추가 : A가 B한테 친구신청을 보내서, from_id : a, to_id : b, is_friend : True를 insert
        Member member = memberRepository.findById(friendRequestDto.getToId()).orElseThrow(() -> new Exception404(ErrorCode.MEMBER_NOT_FOUND));
        Friend friend = new Friend(memberId, member, true);
        Friend savedFriend = friendRepository.save(friend);

        // 2. 역방향 데이터 추가 : from_id : b, to_id : a, is_friend : False를 insert
        Member reversedMember = memberRepository.findById(memberId).orElseThrow(() -> new Exception404(ErrorCode.MEMBER_NOT_FOUND));
        Friend reversedFriend = new Friend(friendRequestDto.getToId(), reversedMember, false);
        friendRepository.save(reversedFriend);

        return FriendRequestResponseDto.builder()
                                    .fromId(savedFriend.getFromId())
                                    .toId(savedFriend.getMember().getId())
                                    .createAt(savedFriend.getCreatedAt())
                                    .modifiedAt(savedFriend.getModifiedAt())
                                    .build();
    }

    @Transactional
    public void cancel(FriendRequestCancelDto friendRequestCancelDto, Long memberId) {
        memberRepository.findById(memberId).orElseThrow(() -> new Exception404(ErrorCode.MEMBER_NOT_FOUND));
        memberRepository.findById(friendRequestCancelDto.getToId()).orElseThrow(() -> new Exception404(ErrorCode.MEMBER_NOT_FOUND));
        isFriendRequestExists(memberId, friendRequestCancelDto);
        friendRepository.deleteFriendship(friendRequestCancelDto.getToId(), memberId);
    }


    @Transactional
    public void accept(FriendRequestAcceptDto acceptRequestDto, Long memberId) {
        isFriendReceivedExists(memberId, acceptRequestDto);
        friendRepository.acceptFriendRequest(memberId, acceptRequestDto.getToId());
    }

    @Transactional
    public void delete(FriendDeleteDto friendDeleteDto, Long memberId) {
        isFriends(memberId, friendDeleteDto);
        friendRepository.deleteFriendship(friendDeleteDto.getToId(), memberId);
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
    private void isFriendRequestExists(Long memberId, FriendRequestDto friendRequestDto) {
        /*
         * 1. A가 B한테 친구 요청을 보냈는데, B가 A한테 친구요청을 보내려고 할 때 안된다고 하는 경우
         * 2. A가 B한테 친구 요청을 보냈는데, 한번 더 동일한 친구 요청(A가 B한테 친구 요청)을 보내는 경우에 안된다고 하는 경우
         */
        if(friendRepository.existsByFromIdAndMemberId(memberId, friendRequestDto.getToId())){
            throw new Exception409(ErrorCode.FRIEND_REQUEST_ALREADY_EXISTS);
        }
    }

    private static void isSelfFriendRequest(Long memberId, FriendRequestDto friendRequestDto) {
        // 3. 자기 자신한테 친구 요청을 보낼 때(A가 A에게 친구 요청), 안된다고 하는 경우
        if (memberId.equals(friendRequestDto.getToId())){
            throw new Exception400(ErrorCode.SELF_FRIEND_REQUEST);
        }
    }

    private void isAlreadyFriend(Long memberId, FriendRequestDto friendRequestDto) {
        //4. 이미 친구인데 친구 요청을 보낼 때, 안된다고 하는 경우
        Long count = friendRepository.isFriend(memberId, friendRequestDto.getToId());
        if (count == 1){
            throw new Exception409(ErrorCode.ALREADY_FRIEND);
        }
    }

    /*
     * 검증 메서드 - 찬구 요청 취소시 검증
     */
    private void isFriendRequestExists(Long memberId, FriendRequestCancelDto friendRequestCancelDto) {
        // 존재하지 않는 친구 신청(내가 보낸 요청)을 취소하려고 하는 경우
        List<FriendResponseDto> friendRequestList = friendRepository.getFriendRequestList(memberId);
        List<Long> friendRequestIdList = friendRequestList.stream()
                .map(FriendResponseDto::getId)
                .collect(Collectors.toList());
        if (!friendRequestIdList.contains(friendRequestCancelDto.getToId())){
            throw new Exception404(ErrorCode.FRIEND_REQUEST_NOT_FOUND);
        }
    }

    /*
     * 검증 메서드 - 찬구 요청 수락시 검증
     */
    private void isFriendReceivedExists(Long memberId, FriendRequestAcceptDto acceptRequestDto) {
        // 존재하지 않는 친구 신청(내가 받은 요청)을 수락하려고 하는 경우
        List<Long> friendReceivedtList = friendRepository.getFriendReceivedList(memberId);
        if (!friendReceivedtList.contains(acceptRequestDto.getToId())){
            throw new Exception404(ErrorCode.FRIEND_REQUEST_NOT_FOUND);
        }
    }

    /*
     * 검증 메서드 - 찬구 삭제시 검증
     */
    private void isFriends(Long memberId, FriendDeleteDto friendDeleteDto) {
        // 친구가 아닌 사람을 삭제하려고 하는 경우
        List<FriendResponseDto> friendList = friendRepository.getFriendList(memberId);
        List<Long> friendIdList = friendList.stream()
                .map(FriendResponseDto::getId)
                .collect(Collectors.toList());
        if (!friendIdList.contains(friendDeleteDto.getToId())){
            throw new Exception404(ErrorCode.FRIEND_NOT_FOUND);
        }
    }
}
