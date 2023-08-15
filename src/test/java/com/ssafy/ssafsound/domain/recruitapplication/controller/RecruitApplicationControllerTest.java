package com.ssafy.ssafsound.domain.recruitapplication.controller;

import com.ssafy.ssafsound.global.docs.ControllerTest;
import com.ssafy.ssafsound.global.util.fixture.RecruitApplicationFixture;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.ssafy.ssafsound.global.docs.snippet.CookieDescriptionSnippet.requestCookieAccessTokenMandatory;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RecruitApplicationControllerTest extends ControllerTest {

    @DisplayName("리크루트 참여 신청")
    @Test
    void saveRecruitApplication() {
        restDocs
                .cookie(ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(RecruitApplicationFixture.POST_RECRUIT_APPLICATION_REQ_DTO)
                .when().post("/recruits/{recruitId}/application", 1)
                .then().log().all()
                .apply(document("recruitApplication/application",
                        requestCookieAccessTokenMandatory(),
                        pathParameters(
                                parameterWithName("recruitId").description("리크루트 아이디")
                        ),
                        requestFields(
                                fieldWithPath("recruitType").type(JsonFieldType.STRING).description("리크루트 작성자가 선택한 자신의 역할군, 메타데이터-리크루트 목록 조회 참고"),
                                fieldWithPath("contents[]").type(JsonFieldType.ARRAY).description("리크루트 등록자 질문에 대한 사용자 답변, [1개 필수]")
                        ),
                        getEnvelopPatternWithNoContent())
                )
                .expect(status().isOk());
    }

    @DisplayName("리크루트 등록자 참여 신청 수락")
    @Test
    void approveRecruitApplicationByRegister() {
        restDocs
                .cookie(ACCESS_TOKEN)
                .when().patch("/recruit-applications/{recruitApplicationId}/approve", 1)
                .then().log().all()
                .apply(document("recruitApplication/application-approve",
                        requestCookieAccessTokenMandatory(),
                        pathParameters(
                                parameterWithName("recruitApplicationId").description("리크루트 참여신청 PK")
                        ),
                        getEnvelopPatternWithNoContent())
                )
                .expect(status().isOk());
    }

    @DisplayName("리크루트 신청자 참여 확정")
    @Test
    void joinRecruitApplication() {
        restDocs
                .cookie(ACCESS_TOKEN)
                .when().patch("/recruit-applications/{recruitApplicationId}/join", 1)
                .then().log().all()
                .apply(document("recruitApplication/application-join",
                        requestCookieAccessTokenMandatory(),
                        pathParameters(
                                parameterWithName("recruitApplicationId").description("리크루트 참여신청 PK")
                        ),
                        getEnvelopPatternWithNoContent())
                )
                .expect(status().isOk());
    }

    @DisplayName("리크루트 참여 신청 거절")
    @Test
    void rejectRecruitApplication() {
        restDocs
                .cookie(ACCESS_TOKEN)
                .when().patch("/recruit-applications/{recruitApplicationId}/reject", 1)
                .then().log().all()
                .apply(document("recruitApplication/application-reject",
                        requestCookieAccessTokenMandatory(),
                        pathParameters(
                                parameterWithName("recruitApplicationId").description("리크루트 참여신청 PK")
                        ),
                        getEnvelopPatternWithNoContent())
                )
                .expect(status().isOk());
    }

    @DisplayName("리크루트 신청자 참여 신청 취소")
    @Test
    void cancelRecruitApplicationByParticipant() {
        restDocs
                .cookie(ACCESS_TOKEN)
                .when().patch("/recruit-applications/{recruitApplicationId}/cancel", 1)
                .then().log().all()
                .apply(document("recruitApplication/application-cancel",
                        requestCookieAccessTokenMandatory(),
                        pathParameters(
                                parameterWithName("recruitApplicationId").description("리크루트 참여신청 PK")
                        ),
                        getEnvelopPatternWithNoContent())
                )
                .expect(status().isOk());
    }

    @DisplayName("리크루트 참여자 목록 조회")
    @Test
    void getRecruitParticipants() {
        doReturn(RecruitApplicationFixture.GET_RECRUIT_PARTICIPANTS_RES_DTO)
                .when(recruitApplicationService)
                .getRecruitParticipants(any());

        restDocs
                .when().get("/recruits/{recruitId}/members", 1)
                .then().log().all()
                .apply(document("recruitApplication/joinMembers",
                        pathParameters(
                                parameterWithName("recruitId").description("리크루트 PK")
                        ),
                        getEnvelopPatternWithData()
                                .andWithPrefix("data.recruitTypes.*.",
                                        fieldWithPath("limit").type(JsonFieldType.NUMBER).description("인원 제한 수")
                                ).andWithPrefix("data.recruitTypes.*.members[].",
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("참여자 PK"),
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("참여자 닉네임"),
                                        fieldWithPath("isMajor").type(JsonFieldType.BOOLEAN).description("전공자 여부")
                                ).andWithPrefix("data.recruitTypes.*.members[].ssafyInfo.",
                                        fieldWithPath("semester").type(JsonFieldType.NUMBER).description("참여자 싸피 기수 (1~10)"),
                                        fieldWithPath("campus").type(JsonFieldType.STRING).description("참여자 소속 캠퍼스 메타데이터-캠퍼스 목록 조회 참고"),
                                        fieldWithPath("certificationState").type(JsonFieldType.STRING).description("참여자 ssafy 인증 여부 UNCERTIFIED | CERTIFIED"),
                                        fieldWithPath("majorTrack").type(JsonFieldType.STRING).description("전공 트랙 Embedded | Mobile | Python | Java")
                                )
                        )
                )
                .expect(status().isOk());
    }

    @DisplayName("등록자 리크루트 참여신청 목록 조회")
    @Test
    void getRecruitApplications() {
        doReturn(RecruitApplicationFixture.GET_RECRUIT_APPLICATIONS_RES_DTO)
                .when(recruitApplicationService)
                .getRecruitApplications(any(), any());

        restDocs
                .cookie(ACCESS_TOKEN)
                .when().get("/recruit-applications?recruitId=1")
                .then().log().all()
                .apply(document("recruitApplication/register-applications",
                        requestCookieAccessTokenMandatory(),
                        requestParameters(
                                parameterWithName("recruitId").description("리크루트 PK")
                        ),
                        getEnvelopPatternWithData().andWithPrefix("data.recruitApplications[].",
                                fieldWithPath("recruitApplicationId").type(JsonFieldType.NUMBER).description("리크루트 참여 신청 PK"),
                                fieldWithPath("recruitType").type(JsonFieldType.STRING).description("리크루트 참여 신청자가 선택한 자신의 역할군, 메타데이터-리크루트 목록 조회 참고"),
                                fieldWithPath("matchStatus").type(JsonFieldType.STRING).description("매칭 상태 - (WAITING_REGISTER_APPROVE:등록자 수락대기), (WAITING_APPLICANT:신청자 최종 수락 대기), (DONE:매칭 성공), (REJECT:매칭 거절),  (CANCEL:매칭취소)"),
                                fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("참여자 PK"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("참여자 닉네임"),
                                fieldWithPath("isMajor").type(JsonFieldType.BOOLEAN).description("전공자 여부"),
                                fieldWithPath("question").type(JsonFieldType.STRING).description("등록자 질문"),
                                fieldWithPath("reply").type(JsonFieldType.STRING).description("참여자 답변"),
                                fieldWithPath("isLike").type(JsonFieldType.BOOLEAN).description("등록자 좋아요 여부")
                        ).andWithPrefix("data.recruitApplications[].ssafyInfo.",
                                fieldWithPath("semester").type(JsonFieldType.NUMBER).description("참여자 싸피 기수 (1~10)"),
                                fieldWithPath("campus").type(JsonFieldType.STRING).description("참여자 소속 캠퍼스 메타데이터-캠퍼스 목록 조회 참고"),
                                fieldWithPath("certificationState").type(JsonFieldType.STRING).description("참여자 ssafy 인증 여부 UNCERTIFIED | CERTIFIED"),
                                fieldWithPath("majorTrack").type(JsonFieldType.STRING).description("전공 트랙 Embedded | Mobile | Python | Java")
                        )
                        )
                )
                .expect(status().isOk());
    }

    @DisplayName("등록자 리크루트 참여신청 좋아요")
    @Test
    void toggleRecruitApplicationLike() {
        restDocs
                .cookie(ACCESS_TOKEN)
                .when().post("/recruit-applications/{recruitApplicationId}/like", 1)
                .then().log().all()
                .apply(document("recruitApplication/register-like",
                        requestCookieAccessTokenMandatory(),
                        pathParameters(
                                parameterWithName("recruitApplicationId").description("리크루트 참여 신청 PK")
                        ),
                        getEnvelopPatternWithNoContent())
                )
                .expect(status().isOk());
    }

    @DisplayName("등록자 리크루트 참여신청 상세 조회")
    @Test
    void getRecruitApplicationByIdAndRegisterId() {
        doReturn(RecruitApplicationFixture.APPLICATION1_ELEMENT)
                .when(recruitApplicationService)
                .getRecruitApplicationByIdAndRegisterId(any(), any());

        restDocs
                .cookie(ACCESS_TOKEN)
                .when().get("/recruit-applications/{recruitApplicationId}", 1)
                .then().log().all()
                .apply(document("recruitApplication/detail",
                        requestCookieAccessTokenMandatory(),
                        pathParameters(
                                parameterWithName("recruitApplicationId").description("리크루트 참여 신청 PK")
                        ),
                        getEnvelopPatternWithData().andWithPrefix("data.",
                                fieldWithPath("recruitApplicationId").type(JsonFieldType.NUMBER).description("리크루트 참여 신청 PK"),
                                fieldWithPath("recruitType").type(JsonFieldType.STRING).description("리크루트 참여 신청자가 선택한 자신의 역할군, 메타데이터-리크루트 목록 조회 참고"),
                                fieldWithPath("matchStatus").type(JsonFieldType.STRING).description("매칭 상태 - (WAITING_REGISTER_APPROVE:등록자 수락대기), (WAITING_APPLICANT:신청자 최종 수락 대기), (DONE:매칭 성공), (REJECT:매칭 거절),  (CANCEL:매칭취소)"),
                                fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("참여자 PK"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("참여자 닉네임"),
                                fieldWithPath("isMajor").type(JsonFieldType.BOOLEAN).description("전공자 여부"),
                                fieldWithPath("question").type(JsonFieldType.STRING).description("등록자 질문"),
                                fieldWithPath("reply").type(JsonFieldType.STRING).description("참여자 답변"),
                                fieldWithPath("isLike").type(JsonFieldType.BOOLEAN).description("등록자 좋아요 여부")
                        ).andWithPrefix("data.ssafyInfo.",
                                fieldWithPath("semester").type(JsonFieldType.NUMBER).description("참여자 싸피 기수 (1~10)"),
                                fieldWithPath("campus").type(JsonFieldType.STRING).description("참여자 소속 캠퍼스 메타데이터-캠퍼스 목록 조회 참고"),
                                fieldWithPath("certificationState").type(JsonFieldType.STRING).description("참여자 ssafy 인증 여부 UNCERTIFIED | CERTIFIED"),
                                fieldWithPath("majorTrack").type(JsonFieldType.STRING).description("전공 트랙 Embedded | Mobile | Python | Java"))
                        )
                )
                .expect(status().isOk());
    }
}
