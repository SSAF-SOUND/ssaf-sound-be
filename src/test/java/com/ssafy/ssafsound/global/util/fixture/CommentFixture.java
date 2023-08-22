package com.ssafy.ssafsound.global.util.fixture;

import com.ssafy.ssafsound.domain.comment.dto.AuthorElement;
import com.ssafy.ssafsound.domain.comment.dto.GetCommentElement;
import com.ssafy.ssafsound.domain.comment.dto.GetCommentResDto;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;

import java.time.LocalDateTime;
import java.util.List;

import static com.ssafy.ssafsound.global.util.fixture.MemberFixture.*;

public class CommentFixture {

    public static final AuthorElement AUTHOR_ELEMENT_COMMENT1 = AuthorElement.builder()
            .nickname("김싸피")
            .memberRole("user")
            .ssafyMember(true)
            .isMajor(true)
            .ssafyInfo(CERTIFIED_SSAFY_INFO)
            .build();

    public static final AuthorElement AUTHOR_ELEMENT_COMMENT2 = AuthorElement.builder()
            .nickname("박싸피")
            .memberRole("user")
            .ssafyMember(false)
            .isMajor(false)
            .ssafyInfo(UNCERTIFIED_SSAFY_INFO)
            .build();

    public static final GetCommentElement GET_COMMENT_REPLY_ELEMENT1 = GetCommentElement.builder()
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
            .replies(List.of())
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
}