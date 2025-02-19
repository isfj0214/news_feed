package com.example.news_feed.post.service;

import com.example.news_feed.common.error.ErrorCode;
import com.example.news_feed.common.error.exception.Exception403;
import com.example.news_feed.common.error.exception.Exception404;
import com.example.news_feed.member.entity.Member;
import com.example.news_feed.member.repository.MemberRepository;
import com.example.news_feed.post.dto.request.PostCreateRequestDto;
import com.example.news_feed.post.dto.response.PostResponseDto;
import com.example.news_feed.post.dto.response.PostCreateResponseDto;
import com.example.news_feed.post.entity.Post;
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
<<<<<<< HEAD

        if (!memberId.equals(post.getMember().getId())) {
            throw new Exception403(POST_ACCESS_DENIED);
=======
        //로그인한 멤버의 id와, 해당 게시물에 같이 저장된 멤버id 비교
        //작성한 본인만 수정할 수 있습니다. 예외코드 추가 후 변경하기!!
        if (!memberId.equals(post.getMember().getId())) {
            throw new Exception404(MEMBER_NOT_FOUND);
>>>>>>> c31f1f5baa00f08c7b299c02a5f19e21ff67bbeb
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
    public void deleteById(Long postId, Long memberId, HttpServletRequest request) {

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new Exception404(ErrorCode.POST_NOT_FOUND)
        );
<<<<<<< HEAD

        if (!memberId.equals(post.getMember().getId())) {
            throw new Exception403(POST_ACCESS_DENIED);
=======
        //현재 로그인 한 유저의 엑세스 토큰의 memberId 일치 여부 확인
        if (!memberId.equals(post.getMember().getId())) {
            throw new Exception404(MEMBER_NOT_FOUND);
>>>>>>> c31f1f5baa00f08c7b299c02a5f19e21ff67bbeb
        }
        postRepository.delete(post);

    }
}
