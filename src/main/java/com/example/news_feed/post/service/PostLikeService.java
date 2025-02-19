package com.example.news_feed.post.service;

import com.example.news_feed.common.error.ErrorCode;
import com.example.news_feed.common.error.exception.Exception404;
import com.example.news_feed.common.error.exception.Exception409;
import com.example.news_feed.member.entity.Member;
import com.example.news_feed.member.repository.MemberRepository;
import com.example.news_feed.post.entity.PostLike;
import com.example.news_feed.post.entity.Post;
import com.example.news_feed.post.repository.PostLikeRepository;
import com.example.news_feed.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        PostLike findPostLike = postLikeRepository.findByMember_IdAndPost_Id(memberId, postId).orElse(null);
        if(findPostLike != null){
            throw new Exception409(ErrorCode.ALREADY_LIKED);
        }

        PostLike postLike = new PostLike(findMember, findPost);

        postLikeRepository.save(postLike);
    }

    @Transactional
    public void deleteLike(Long memberId, Long postId){

        Member findMember= memberRepository.findById(memberId).orElseThrow(() -> new Exception404(ErrorCode.MEMBER_NOT_FOUND));
        Post findPost = postRepository.findById(postId).orElseThrow(() -> new Exception404(ErrorCode.POST_NOT_FOUND));

        postLikeRepository.deleteByMember_IdAndPost_Id(memberId, postId);

    }
}
