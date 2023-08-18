package com.ssafy.ssafsound.domain.recruitcomment.controller;

import com.ssafy.ssafsound.global.docs.ControllerTest;
import com.ssafy.ssafsound.global.util.fixture.RecruitCommentFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.ssafy.ssafsound.global.docs.snippet.CookieDescriptionSnippet.requestCookieAccessTokenMandatory;
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
                                fieldWithPath("recruitCommentId").type(JsonFieldType.NUMBER).description("리크루트 덧글 PK"),
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
                        getEnvelopPatternWithNoContent())
                );
    }

    @DisplayName("리크루트 덧글 목록 조회")
    @Test
    void getRecruitComments() {
        doReturn(RecruitCommentFixture.GET_RECRUIT_RES_DTO)
                .when(recruitCommentService)
                .getRecruitComments(any());

        restDocs
                .when().get("/recruits/{recruitId}/comments", 1)
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .apply(document("recruitComment/getLists",
                        pathParameters(
                                parameterWithName("recruitId").description("리크루트 덧글 PK")
                        ),
                        getEnvelopPatternWithData().andWithPrefix("data.recruitComments[].",
                                fieldWithPath("recruitCommentId").type(JsonFieldType.NUMBER).description("리크루트 덧글 PK"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("리크루트 덧글 내용"),
                                fieldWithPath("commentGroup").type(JsonFieldType.NUMBER).description("리크루트 상위 덧글 PK"),
                                fieldWithPath("deletedComment").type(JsonFieldType.BOOLEAN).description("덧글 삭제 여부"),
                                fieldWithPath("children").type(JsonFieldType.ARRAY).optional().description("대댓글 [최대1개]"),
                                fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("참여자 PK"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("참여자 닉네임"),
                                fieldWithPath("ssafyMember").type(JsonFieldType.BOOLEAN).description("싸피생 여부"),
                                fieldWithPath("isMajor").type(JsonFieldType.BOOLEAN).description("전공자 여부"),
                                fieldWithPath("majorTrack").type(JsonFieldType.STRING).description("전공자 트랙"))
                        )
                );
    }
}
