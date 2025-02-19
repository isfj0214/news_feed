package com.example.news_feed.common.error;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // 400 에러
    JWT_ERROR("JWT 처리 중 오류가 발생했습니다.", HttpStatus.BAD_REQUEST),
    SELF_FRIEND_REQUEST("자기 자신에게 친구 요청을 할 수 없습니다.", HttpStatus.BAD_REQUEST),
    SELF_LIKE_REQUEST("자신의 댓글에 좋아요를 남길 수 없습니다.",HttpStatus.BAD_REQUEST),
    SELF_POST_REQUEST("자신의 포스트에 좋아요를 남길 수 없습니다.",HttpStatus.BAD_REQUEST),
    ALREADY_EXIST_LIKE_REQUEST("이미 해당 댓글에 좋아요를 눌렀습니다.",HttpStatus.BAD_REQUEST),

    // 401 에러
    INVALID_PASSWORD("비밀번호가 잘못되었습니다.", HttpStatus.UNAUTHORIZED),
    TOKEN_NOT_PROVIDED("토큰이 제공되지 않았습니다.", HttpStatus.UNAUTHORIZED),
    ACCESS_TOKEN_EXPIRED("만료된 ACCESS 토큰입니다.", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_EXPIRED("만료된 REFRESH 토큰입니다.", HttpStatus.UNAUTHORIZED),
    ACCESS_TOKEN_REQUIRED("ACCESS 토큰이 필요합니다.", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_REQUIRED("REFRESH 토큰이 필요합니다.", HttpStatus.UNAUTHORIZED),

    // 403
    USER_ACCESS_DENIED("유저에 대한 접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    POST_ACCESS_DENIED("게시글에 대한 접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    COMMENT_ACCESS_DENIED("댓글에 대한 접근 권한이 없습니다.", HttpStatus.FORBIDDEN),

    // 404 에러
    COMMENT_NOT_FOUND("commentId에 해당하는 댓글이 없습니다.", HttpStatus.NOT_FOUND),
    LIKECOMMENT_NOT_FOUND("likecommentId에 해당하는 댓글이 없습니다.",HttpStatus.NOT_FOUND),
    POST_NOT_FOUND("postId에 해당하는 일정이 없습니다.", HttpStatus.NOT_FOUND),
    MEMBER_NOT_FOUND("memberId에 해당하는 멤버가 없습니다.", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND_BY_EMAIL("해당 이메일로 등록된 유저가 없습니다.", HttpStatus.NOT_FOUND),
    FRIEND_REQUEST_NOT_FOUND("존재하지 않는 친구 신청 정보입니다.", HttpStatus.NOT_FOUND),
    FRIEND_NOT_FOUND("존재하지 않는 친구입니다.", HttpStatus.NOT_FOUND),

    //409 에러
    EMAIL_ALREADY_EXISTS("이미 사용 중인 이메일입니다.", HttpStatus.CONFLICT),
    ALREADY_LOGGED_IN("이미 로그인된 상태입니다.", HttpStatus.CONFLICT),
    USING_PASSWORD("이미 사용 중인 비밀번호 입니다.", HttpStatus.CONFLICT),
    FRIEND_REQUEST_ALREADY_EXISTS("이미 존재하는 친구 요청이 있습니다.", HttpStatus.CONFLICT),
    ALREADY_FRIEND("이미 친구로 등록된 친구입니다.", HttpStatus.CONFLICT),
    ALREADY_LIKED("이미 좋아요를 눌렀습니다.", HttpStatus.CONFLICT);


    private String message;
    private HttpStatus httpStatus;

    private ErrorCode(String message, HttpStatus httpStatus){
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus(){
        return httpStatus;
    }
}
