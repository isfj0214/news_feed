package com.example.news_feed.post.service;

import com.example.news_feed.common.error.ErrorCode;
import com.example.news_feed.common.error.exception.Exception400;
import com.example.news_feed.common.error.exception.Exception404;
import com.example.news_feed.common.error.exception.Exception409;
import com.example.news_feed.member.entity.Member;
import com.example.news_feed.member.repository.MemberRepository;
import com.example.news_feed.post.dto.response.PostLikeResponseDto;
import com.example.news_feed.post.entity.PostLike;
import com.example.news_feed.post.entity.Post;
import com.example.news_feed.post.repository.PostLikeRepository;
import com.example.news_feed.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Transactional
    public void createLike(Long memberId, Long postId){

        Member findMember= memberRepository.findById(memberId).orElseThrow(() -> new Exception404(ErrorCode.MEMBER_NOT_FOUND));

        Post findPost = postRepository.findById(postId).orElseThrow(() -> new Exception404(ErrorCode.POST_NOT_FOUND));
        if(memberId.equals(findPost.getMember().getId())){
            throw new Exception409(ErrorCode.SELF_POST_REQUEST);
        }

        PostLike findPostLike = postLikeRepository.findByMember_IdAndPost_Id(memberId, postId).orElse(null);
        if(findPostLike != null){
            throw new Exception409(ErrorCode.ALREADY_LIKED);
        }

        findPost.addLikeCount();

        PostLike postLike = new PostLike(findMember, findPost);

        postLikeRepository.save(postLike);
    }

    @Transactional(readOnly = true)
    public List<PostLikeResponseDto> findAllLikes(Long postId){

        Post findPost = postRepository.findById(postId).orElseThrow(() -> new Exception404(ErrorCode.POST_NOT_FOUND));

        List<PostLike> postLikeResponseDtos = postLikeRepository.findAllByPost_Id(postId);

        return postLikeResponseDtos.stream().map(
                postLike -> new PostLikeResponseDto(postLike.getMember().getId())
        ).collect(Collectors.toList());
    }

    @Transactional
    public void deleteLike(Long memberId, Long postId){

        Member findMember= memberRepository.findById(memberId).orElseThrow(() -> new Exception404(ErrorCode.MEMBER_NOT_FOUND));
        Post findPost = postRepository.findById(postId).orElseThrow(() -> new Exception404(ErrorCode.POST_NOT_FOUND));

        PostLike findPostLike = postLikeRepository.findByMember_IdAndPost_Id(memberId, postId).orElseThrow(() -> new Exception400(ErrorCode.LIKE_NOT_FOUND_REQUEST));
        findPost.minusLikeCount();

        postLikeRepository.deleteByMember_IdAndPost_Id(memberId, postId);

    }
}
