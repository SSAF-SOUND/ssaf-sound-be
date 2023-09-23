package com.ssafy.ssafsound.global.util.fixture;

import com.ssafy.ssafsound.domain.comment.domain.Comment;
import com.ssafy.ssafsound.domain.comment.dto.*;
import com.ssafy.ssafsound.domain.member.dto.AuthorElement;
import com.ssafy.ssafsound.domain.post.dto.PostCommonLikeResDto;

import java.time.LocalDateTime;
import java.util.List;

import static com.ssafy.ssafsound.global.util.fixture.PostFixture.POST_FIXTURE1;

public class CommentFixture {

    private static final MemberFixture memberFixture = new MemberFixture();

    public static final AuthorElement AUTHOR_ELEMENT_COMMENT1 = AuthorElement.builder()
            .memberId(1L)
            .nickname("김싸피")
            .memberRole("user")
            .ssafyMember(true)
            .isMajor(true)
            .ssafyInfo(memberFixture.createCertifiedSSAFYInfo())
            .build();

    public static final AuthorElement AUTHOR_ELEMENT_COMMENT2 = AuthorElement.builder()
            .memberId(2L)
            .nickname("박싸피")
            .memberRole("user")
            .ssafyMember(false)
            .isMajor(false)
            .ssafyInfo(null)
            .build();

    public static final GetCommentReplyElement GET_COMMENT_REPLY_ELEMENT1 = GetCommentReplyElement.builder()
            .commentId(1L)
            .content("안녕하세요")
            .likeCount(10)
            .createdAt(LocalDateTime.now())
            .anonymity(false)
            .modified(false)
            .liked(false)
            .mine(false)
            .deletedComment(false)
            .author(AUTHOR_ELEMENT_COMMENT1)
            .build();

    public static final GetCommentElement GET_COMMENT_ELEMENT1 = GetCommentElement.builder()
            .commentId(1L)
            .content("반가워요")
            .likeCount(3)
            .createdAt(LocalDateTime.now())
            .anonymity(true)
            .modified(false)
            .liked(true)
            .mine(false)
            .deletedComment(false)
            .author(AUTHOR_ELEMENT_COMMENT2)
            .replies(List.of(GET_COMMENT_REPLY_ELEMENT1))
            .build();

    public static final GetCommentResDto GET_COMMENT_RES_DTO1 = GetCommentResDto.builder()
            .comments(List.of(GET_COMMENT_ELEMENT1))
            .build();

    public static final CommentIdElement COMMENT_ID_ELEMENT = CommentIdElement.builder()
            .commentId(1L)
            .build();

    public static final PostCommentWriteReqDto POST_COMMENT_WRITE_REQ_DTO = PostCommentWriteReqDto.builder()
            .content("댓글 내용 입니다.")
            .anonymity(false)
            .build();

    public static final PostCommentWriteReqDto POST_COMMENT_REPLY_WRITE_REQ_DTO = PostCommentWriteReqDto.builder()
            .content("대댓글 내용 입니다.")
            .anonymity(true)
            .build();

    public static final PatchCommentUpdateReqDto PUT_COMMENT_UPDATE_REQ_DTO = PatchCommentUpdateReqDto.builder()
            .content("수정된 댓글 내용 입니다.")
            .anonymity(true)
            .build();

    public static final PostCommonLikeResDto POST_COMMON_LIKE_RES_DTO = PostCommonLikeResDto.builder()
            .likeCount(12)
            .liked(true)
            .build();

    public static final Comment COMMENT_FIXTURE_1 = Comment.builder()
            .id(1L)
            .member(memberFixture.createMember())
            .deletedComment(Boolean.FALSE)
            .anonymity(Boolean.FALSE)
            .post(POST_FIXTURE1)
            .content("화이팅입니다.")
            .build();
}