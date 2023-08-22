package com.ssafy.ssafsound.domain.comment.controller;

import com.ssafy.ssafsound.global.docs.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.ssafy.ssafsound.global.docs.snippet.CookieDescriptionSnippet.requestCookieAccessTokenMandatory;
import static com.ssafy.ssafsound.global.docs.snippet.CookieDescriptionSnippet.requestCookieAccessTokenOptional;
import static com.ssafy.ssafsound.global.util.fixture.CommentFixture.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

class CommentControllerTest extends ControllerTest {

    @Test
    @DisplayName("댓글 목록 조회(페이지네이션 적용X)")
    void findComments() {
        doReturn(GET_COMMENT_RES_DTO1)
                .when(commentService)
                .findComments(any(), any());

        restDocs.cookie(ACCESS_TOKEN)
                .when().get("/comments?postId={postId}", 1L)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .apply(document("comment/write-comment",
                                requestCookieAccessTokenOptional(),
                                requestParameters(
                                        parameterWithName("postId").description("댓글을 작성하려는 게시글의 고유 ID")
                                ),
                                getEnvelopPatternWithData().andWithPrefix("data.",
                                        fieldWithPath("comments").type(JsonFieldType.ARRAY).description("댓글 목록(대댓글 포함)")
                                ).andWithPrefix("data.comments[].",
                                        fieldWithPath("commentId").type(JsonFieldType.NUMBER).description("댓글의 고유 ID"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("댓글의 내용"),
                                        fieldWithPath("likeCount").type(JsonFieldType.NUMBER).description("댓글에 달린 좋아요 개수"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("댓글 작성일"),
                                        fieldWithPath("anonymity").type(JsonFieldType.BOOLEAN).description("댓글의 작성자가 익명인지 여부"),
                                        fieldWithPath("modified").type(JsonFieldType.BOOLEAN).description("댓글이 수정되었는지 여부"),
                                        fieldWithPath("liked").type(JsonFieldType.BOOLEAN).description("로그인한 사용자일 때 이 댓글이 내가 좋아요를 누른 댓글인지 여부"),
                                        fieldWithPath("mine").type(JsonFieldType.BOOLEAN).description("로그인한 사용자일 때 이 댓글이 내가 작성한 댓글인지 여부"),
                                        fieldWithPath("deletedComment").type(JsonFieldType.BOOLEAN).description("삭제된 댓글인지 여부(삭제된 댓글이어도 대댓글의 내용은 출력되어야 하기 때문)"),
                                        fieldWithPath("author").type(JsonFieldType.OBJECT).description("댓글 작성자의 정보"),
                                        fieldWithPath("replies").type(JsonFieldType.ARRAY).description("댓글의 대댓글(대댓글의 최대 Depth는 1)").optional()
                                ).andWithPrefix("data.comments[].author.",
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("댓글 작성자의 고유 ID(단, 익명이면 -1)"),
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("댓글 작성자의 닉네임"),
                                        fieldWithPath("memberRole").type(JsonFieldType.STRING).description("댓글 작성자의 사용자 권한").optional(),
                                        fieldWithPath("ssafyMember").type(JsonFieldType.BOOLEAN).description("댓글 작성자가 SSAFY 유저인지 나타내는 필드 값").optional(),
                                        fieldWithPath("isMajor").type(JsonFieldType.BOOLEAN).description("댓글 작성자가 전공자인지 여부, true면 전공자, false면 비전공자").optional(),
                                        fieldWithPath("ssafyInfo").type(JsonFieldType.OBJECT).description("댓글 작성자가 선택한 SSAFY 정보 단, SSAFY 인증을 받지 않아도, 해당 필드는 존재하지만 익명이면 null값이 들어감.").optional()
                                ).andWithPrefix("data.comments[].author.ssafyInfo.",
                                        fieldWithPath("semester").type(JsonFieldType.NUMBER).description("작성자의 SSAFY 기수를 의미").optional(),
                                        fieldWithPath("campus").type(JsonFieldType.STRING).description("작성자의 SSAFY 캠퍼스를 의미").optional(),
                                        fieldWithPath("certificationState").type(JsonFieldType.STRING).description("작성자의 SSAFY 인증 여부를 의미함, CERTIFIED | UNCERTIFIED").optional(),
                                        fieldWithPath("majorTrack").type(JsonFieldType.STRING).description("작성자의 전공트랙 정보를 의미, Java | Python | Mobile | Embedded").optional()
                                ).andWithPrefix("data.comments[].replies[].",
                                        fieldWithPath("commentId").type(JsonFieldType.NUMBER).description("대댓글의 고유 ID"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("대댓글의 내용"),
                                        fieldWithPath("likeCount").type(JsonFieldType.NUMBER).description("대댓글에 달린 좋아요 개수"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("대댓글의 작성일"),
                                        fieldWithPath("anonymity").type(JsonFieldType.BOOLEAN).description("대댓글의 작성자가 익명인지 여부"),
                                        fieldWithPath("modified").type(JsonFieldType.BOOLEAN).description("대댓글이 수정되었는지 여부"),
                                        fieldWithPath("liked").type(JsonFieldType.BOOLEAN).description("로그인한 사용자일 때 이 대댓글이 내가 좋아요를 누른 대댓글인지 여부"),
                                        fieldWithPath("mine").type(JsonFieldType.BOOLEAN).description("로그인한 사용자일 때 이 대댓글이 내가 작성한 대댓글인지 여부"),
                                        fieldWithPath("deletedComment").type(JsonFieldType.BOOLEAN).description("삭제된 대댓글인지 여부"),
                                        fieldWithPath("author").type(JsonFieldType.OBJECT).description("대댓글 작성자의 정보")
                                ).andWithPrefix("data.comments[].replies[].author.",
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("대댓글 작성자의 고유 ID(단, 익명이면 -1)"),
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("대댓글 작성자의 닉네임"),
                                        fieldWithPath("memberRole").type(JsonFieldType.STRING).description("대댓글 작성자의 사용자 권한").optional(),
                                        fieldWithPath("ssafyMember").type(JsonFieldType.BOOLEAN).description("대댓글 작성자가 SSAFY 유저인지 나타내는 필드 값").optional(),
                                        fieldWithPath("isMajor").type(JsonFieldType.BOOLEAN).description("대댓글 작성자가 전공자인지 여부, true면 전공자, false면 비전공자").optional(),
                                        fieldWithPath("ssafyInfo").type(JsonFieldType.OBJECT).description("대댓글 작성자가 선택한 SSAFY 정보 단, SSAFY 인증을 받지 않아도, 해당 필드는 존재하지만 익명이면 null값이 들어감.").optional()
                                ).andWithPrefix("data.comments[].replies[].author.ssafyInfo.",
                                        fieldWithPath("semester").type(JsonFieldType.NUMBER).description("작성자의 SSAFY 기수를 의미").optional(),
                                        fieldWithPath("campus").type(JsonFieldType.STRING).description("작성자의 SSAFY 캠퍼스를 의미").optional(),
                                        fieldWithPath("certificationState").type(JsonFieldType.STRING).description("작성자의 SSAFY 인증 여부를 의미함, CERTIFIED | UNCERTIFIED").optional(),
                                        fieldWithPath("majorTrack").type(JsonFieldType.STRING).description("작성자의 전공트랙 정보를 의미, Java | Python | Mobile | Embedded").optional()
                                )
                        )
                );
    }

    @Test
    @DisplayName("댓글 쓰기")
    void writeComment() {
        doReturn(COMMENT_ID_ELEMENT)
                .when(commentService)
                .writeComment(any(), any(), any());

        restDocs.cookie(ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(POST_COMMENT_WRITE_REQ_DTO)
                .when().post("/comments?postId={postId}", 1L)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .apply(document("comment/write-comment",
                                requestCookieAccessTokenMandatory(),
                                requestParameters(
                                        parameterWithName("postId").description("댓글을 작성할 게시글의 고유 ID")
                                ),
                                requestFields(
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용"),
                                        fieldWithPath("anonymity").type(JsonFieldType.BOOLEAN).description("댓글 작성자의 익명 여부를 나타내는 필드")
                                ),
                                getEnvelopPatternWithData().andWithPrefix("data.",
                                        fieldWithPath("commentId").type(JsonFieldType.NUMBER).description("작성한 댓글 ID")
                                )
                        )
                );
    }

    @Test
    @DisplayName("대댓글 쓰기")
    void writeCommentReply() {
        doReturn(COMMENT_ID_ELEMENT)
                .when(commentService)
                .writeCommentReply(any(), any(), any(), any());

        restDocs.cookie(ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(POST_COMMENT_REPLY_WRITE_REQ_DTO)
                .when().post("/comments/reply?commentId={commentId}&postId={postId}", 1L, 1L)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .apply(document("comment/write-comment-reply",
                                requestCookieAccessTokenMandatory(),
                                requestParameters(
                                        parameterWithName("commentId").description("대댓글을 작성할 댓글의 고유 ID"),
                                        parameterWithName("postId").description("대댓글을 작성할 게시글의 고유 ID")
                                ),
                                requestFields(
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용"),
                                        fieldWithPath("anonymity").type(JsonFieldType.BOOLEAN).description("댓글 작성자의 익명 여부를 나타내는 필드")
                                ),
                                getEnvelopPatternWithData().andWithPrefix("data.",
                                        fieldWithPath("commentId").type(JsonFieldType.NUMBER).description("작성한 대댓글 ID")
                                )
                        )
                );
    }


    @Test
    void updateComment() {
    }

    @Test
    void likeComment() {
    }

    @Test
    void deleteComment() {
    }
}