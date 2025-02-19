package com.example.news_feed.post.service;

import com.example.news_feed.comment.repository.CommentRepository;
import com.example.news_feed.comment.repository.LikeCommentRepository;
import com.example.news_feed.common.error.ErrorCode;
import com.example.news_feed.common.error.exception.Exception403;
import com.example.news_feed.common.error.exception.Exception404;
import com.example.news_feed.member.entity.Member;
import com.example.news_feed.member.repository.MemberRepository;
import com.example.news_feed.post.dto.request.PostCreateRequestDto;
import com.example.news_feed.post.dto.response.PostResponseDto;
import com.example.news_feed.post.dto.response.PostCreateResponseDto;
import com.example.news_feed.post.entity.Post;
import com.example.news_feed.post.repository.PostLikeRepository;
import com.example.news_feed.post.repository.PostRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.news_feed.common.error.ErrorCode.MEMBER_NOT_FOUND;
import static com.example.news_feed.common.error.ErrorCode.POST_ACCESS_DENIED;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;
    private final LikeCommentRepository likeCommentRepository;
    private final MemberRepository memberRepository;


    //게시글 생성
    @Transactional
    public PostCreateResponseDto createPost(PostCreateRequestDto dto, Long memberId) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(
                () -> new Exception404(MEMBER_NOT_FOUND)
        );

        Post post = new Post(dto.getTitle(), dto.getContent(), findMember);
        Post saved = postRepository.save(post);
        return new PostCreateResponseDto(
                saved.getId(),
                saved.getTitle(),
                saved.getContent(),
                saved.getCreatedAt(),
                saved.getModifiedAt()
        );
    }

    //게시글 전체 조회
    @Transactional(readOnly = true)
    public List<PostResponseDto> findAll(Pageable pageable) {

        Page<Post> posts = postRepository.findAll(pageable);
        List<PostResponseDto> dtos = new ArrayList<>();

        for (Post post : posts) {
            dtos.add(new PostResponseDto(
                    post.getId(),
                    post.getTitle(),
                    post.getContent(),
                    post.getCreatedAt(),
                    post.getModifiedAt()
            ));
        }
        return dtos;
    }

    @Transactional(readOnly = true)
    public PostResponseDto findById(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new Exception404(ErrorCode.POST_NOT_FOUND)
        );
        return new PostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getModifiedAt()
        );

    }

    public PostResponseDto update(Long postId, Long memberId, PostCreateRequestDto dto) {


        Post post = postRepository.findById(postId).orElseThrow(
                () -> new Exception404(ErrorCode.POST_NOT_FOUND)
        );

        if (!memberId.equals(post.getMember().getId())) {
            throw new Exception403(POST_ACCESS_DENIED);
        }

        post.update(dto.getTitle(), dto.getContent());
        postRepository.save(post);
        return new PostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getModifiedAt()
        );


    }

    @Transactional
    public void deleteById(Long postId, Long memberId) {
        List<Long> commentIds = commentRepository.findIdsByPostId(postId);

        if (!commentIds.isEmpty()) {
            likeCommentRepository.deleteByCommentIds(commentIds);

            // 3️⃣ 댓글 삭제
            commentRepository.deleteByPostId(postId);
        }

        // 4️⃣ 게시물 좋아요 삭제
        postLikeRepository.deleteByPostId(postId);

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new Exception404(ErrorCode.POST_NOT_FOUND)
        );

        if (!memberId.equals(post.getMember().getId())) {
            throw new Exception403(POST_ACCESS_DENIED);
        }
        postRepository.delete(post);

    }
}
