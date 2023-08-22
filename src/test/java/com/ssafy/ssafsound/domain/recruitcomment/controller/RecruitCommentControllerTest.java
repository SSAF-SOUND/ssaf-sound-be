package com.ssafy.ssafsound.domain.recruitcomment.controller;

import com.ssafy.ssafsound.global.docs.ControllerTest;
import com.ssafy.ssafsound.global.util.fixture.RecruitCommentFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.ssafy.ssafsound.global.docs.snippet.CookieDescriptionSnippet.requestCookieAccessTokenMandatory;
import static com.ssafy.ssafsound.global.docs.snippet.CookieDescriptionSnippet.requestCookieAccessTokenOptional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

public class RecruitCommentControllerTest extends ControllerTest {

    @DisplayName("리크루트 덧글등록")
    @Test
    void saveRecruitComment() {
        doReturn(RecruitCommentFixture.POST_RECRUIT_COMMENT_RES_DTO)
                .when(recruitCommentService)
                .saveRecruitComment(any(), any(), any());

        restDocs
                .cookie(ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(RecruitCommentFixture.POST_RECRUIT_COMMENT_REQ_DTO)
                .when().post("/recruits/{recruitId}/comments", 1)
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .apply(document("recruitComment/save",
                        requestCookieAccessTokenMandatory(),
                        pathParameters(
                                parameterWithName("recruitId").description("리크루트 아이디")
                        ),
                        requestFields(
                                fieldWithPath("content").type(JsonFieldType.STRING).description("리크루트 덧글 내용"),
                                fieldWithPath("commentGroup").type(JsonFieldType.NUMBER).description("default: -1, 대댓글인 경우 해당 상위 덧글의 PK")
                        ),
                        getEnvelopPatternWithData().andWithPrefix("data.",
                                fieldWithPath("commentId").type(JsonFieldType.NUMBER).description("리크루트 덧글 PK"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("리크루트 덧글 PK"),
                                fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("작성자 PK"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("작성자 닉네임"),
                                fieldWithPath("commentGroup").type(JsonFieldType.NUMBER).description("상위 덧글의 ID"))
                        )
                );
    }

    @DisplayName("리크루트 덧글 삭제")
    @Test
    void deleteRecruitComment() {
        restDocs
                .cookie(ACCESS_TOKEN)
                .when().delete("/recruit-comments/{recruitCommentId}", 1)
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .apply(document("recruitComment/delete",
                        requestCookieAccessTokenMandatory(),
                        pathParameters(
                                parameterWithName("recruitCommentId").description("리크루트 덧글 PK")
                        ),
                        getEnvelopPatternWithNoContent())
                );
    }

    @DisplayName("리크루트 덧글 업데이트")
    @Test
    void updateRecruitComment() {
        restDocs
                .cookie(ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(RecruitCommentFixture.PATCH_RECRUIT_COMMENT_REQ_DTO)
                .when().patch("/recruit-comments/{recruitCommentId}", 1)
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .apply(document("recruitComment/update",
                        requestCookieAccessTokenMandatory(),
                        pathParameters(
                                parameterWithName("recruitCommentId").description("리크루트 덧글 PK")
                        ),
                        requestFields(
                                fieldWithPath("content").type(JsonFieldType.STRING).description("리크루트 덧글 내용")
                        ),
                        getEnvelopPatternWithNoContent())
                );
    }

    @DisplayName("리크루트 덧글 좋아요")
    @Test
    void toggleRecruitCommentLike() {
        doReturn(RecruitCommentFixture.POST_RECRUIT_COMMENT_LIKE_RES_DTO)
            .when(recruitCommentService)
                .toggleRecruitCommentLike(any(), any());

        restDocs
                .cookie(ACCESS_TOKEN)
                .when().post("/recruit-comments/{recruitCommentId}/like", 1)
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .apply(document("recruitComment/like",
                        requestCookieAccessTokenMandatory(),
                        pathParameters(
                                parameterWithName("recruitCommentId").description("리크루트 덧글 PK")
                        ),
                        getEnvelopPatternWithData().andWithPrefix("data.",
                            fieldWithPath("likeCount").type(JsonFieldType.NUMBER).description("리크루트 좋아요 갯수"),
                            fieldWithPath("liked").type(JsonFieldType.BOOLEAN).description("리크루트 좋아요 여부")
                        ))
                );
    }

    @DisplayName("리크루트 덧글 목록 조회")
    @Test
    void getRecruitComments() {
        doReturn(RecruitCommentFixture.GET_RECRUIT_RES_DTO)
                .when(recruitCommentService)
                .getRecruitComments(any(), any());

        restDocs
                .when().get("/recruits/{recruitId}/comments", 1)
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .apply(document("recruitComment/getLists",
                        requestCookieAccessTokenOptional(),
                        pathParameters(
                                parameterWithName("recruitId").description("리크루트 덧글 PK")
                        ),
                        getEnvelopPatternWithData().andWithPrefix("data.recruitComments[].",
                                fieldWithPath("commentId").type(JsonFieldType.NUMBER).description("리크루트 덧글 PK"),
                                fieldWithPath("likeCount").type(JsonFieldType.NUMBER).description("리크루트 덧글 좋아요 갯수"),
                                fieldWithPath("liked").type(JsonFieldType.BOOLEAN).description("조회자 리크루트 좋아요 여부"),
                                fieldWithPath("mine").type(JsonFieldType.BOOLEAN).description("자기 자신이 쓴 좋아요 여부"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("리크루트 덧글 생성일"),
                                fieldWithPath("modified").type(JsonFieldType.BOOLEAN).description("리크루트 덧글 수정여부"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("리크루트 덧글 내용"),
                                fieldWithPath("commentGroup").type(JsonFieldType.NUMBER).description("리크루트 상위 덧글 PK"),
                                fieldWithPath("deletedComment").type(JsonFieldType.BOOLEAN).description("덧글 삭제 여부"),
                                fieldWithPath("replies").type(JsonFieldType.ARRAY).optional().description("대댓글 [최대1개]"),
                                fieldWithPath("author.memberId").type(JsonFieldType.NUMBER).description("참여자 PK"),
                                fieldWithPath("author.nickname").type(JsonFieldType.STRING).description("참여자 닉네임"),
                                fieldWithPath("author.memberRole").type(JsonFieldType.STRING).description("참여자 권한"),
                                fieldWithPath("author.ssafyMember").type(JsonFieldType.BOOLEAN).description("싸피생 여부"),
                                fieldWithPath("author.isMajor").type(JsonFieldType.BOOLEAN).description("전공자 여부"),
                                fieldWithPath("author.ssafyInfo.semester").type(JsonFieldType.NUMBER).description("기수"),
                                fieldWithPath("author.ssafyInfo.campus").type(JsonFieldType.STRING).description("캠퍼스"),
                                fieldWithPath("author.ssafyInfo.certificationState").type(JsonFieldType.STRING).description("인증상태"),
                                fieldWithPath("author.ssafyInfo.majorTrack").type(JsonFieldType.STRING).description("전공자 트랙"))
                        )
                );
    }
}
