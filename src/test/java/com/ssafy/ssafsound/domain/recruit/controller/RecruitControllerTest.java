package com.ssafy.ssafsound.domain.recruit.controller;

import com.ssafy.ssafsound.domain.recruit.dto.PostRecruitResDto;
import com.ssafy.ssafsound.global.docs.ControllerTest;
import com.ssafy.ssafsound.global.util.fixture.RecruitFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.ssafy.ssafsound.global.docs.snippet.CookieDescriptionSnippet.requestCookieAccessTokenMandatory;

import static com.ssafy.ssafsound.global.docs.snippet.CookieDescriptionSnippet.requestCookieAccessTokenOptional;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class RecruitControllerTest extends ControllerTest {

    @DisplayName("리크루트 등록")
    @Test
    void saveRecruit() {
        doReturn(new PostRecruitResDto(1L))
                .when(recruitService)
                .saveRecruit(any(), any());

        restDocs
                .cookie(ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(RecruitFixture.RECRUIT_POST_REQ_DTO)
                .when().post("/recruits")
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .apply(document("recruit/saveRecruit",
                        requestCookieAccessTokenMandatory(),
                        requestFields(
                                fieldWithPath("category").type(JsonFieldType.STRING).description("PROJECT | STUDY"),
                                fieldWithPath("recruitEnd").type(JsonFieldType.STRING).description("yyyy-MM-dd 모집 종료 일자, 당일 날짜 이후의 값으로만 설정 가능"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("리크루트 모집글 제목, 글자 수 제한 50자"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("리크루트 본문, 글자 수 제한 3000자, html 포함 4000자"),
                                fieldWithPath("contactURI").type(JsonFieldType.STRING).description("리크루트 작성자와 연락 가능한 오픈톡 링크"),
                                fieldWithPath("registerRecruitType").type(JsonFieldType.STRING).description("리크루트 작성자가 선택한 자신의 역할군, 메타데이터-리크루트 목록 조회 참고"),
                                fieldWithPath("skills[]").type(JsonFieldType.ARRAY).optional().description("리크루트와 연관된 기술 스택, 메타데이터-스킬 목록 조회 참고"),
                                fieldWithPath("questions[]").type(JsonFieldType.ARRAY).optional().description("리크루트 참석자에게 할 질문, 1개만 등록 가능"),
                                fieldWithPath("limitations[].recruitType").type(JsonFieldType.STRING).description("리크루트 모집파트, 메타데이터-리크루트 목록 조회 참고"),
                                fieldWithPath("limitations[].limit").type(JsonFieldType.NUMBER).description("인원 제한 1명이상 10명 이하")
                        ),
                        getEnvelopPatternWithData().andWithPrefix("data.",
                                fieldWithPath("recruitId").type(JsonFieldType.NUMBER).description("생성된 리크루트 ID")
                        ))
                );
    }

    @DisplayName("리크루트 스크랩 토글")
    @Test
    void toggleRecruitScrap() {
        doReturn(RecruitFixture.RECRUIT_SCRAP_RES_DTO)
                .when(recruitService)
                .toggleRecruitScrap(any(), any());

        restDocs
                .cookie(ACCESS_TOKEN)
                .when().post("/recruits/{recruitId}/scrap", 1L)
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .apply(document("recruit/toggleRecruitScrap",
                        requestCookieAccessTokenMandatory(),
                        pathParameters(
                                parameterWithName("recruitId").description("리크루트 아이디")
                        ),
                        getEnvelopPatternWithData().andWithPrefix("data.",
                                fieldWithPath("scrapCount").type(JsonFieldType.NUMBER).description("전체 스크랩 갯수"),
                                fieldWithPath("scraped").type(JsonFieldType.BOOLEAN).description("스크랩 여부")
                            )

                        )
                );
    }

    @DisplayName("리크루트 모집 완료 처리")
    @Test
    void expiredRecruit() {
        restDocs
                .cookie(ACCESS_TOKEN)
                .when().post("/recruits/{recruitId}/expired", 1L)
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .apply(document("recruit/expiredRecruit",
                        requestCookieAccessTokenMandatory(),
                        pathParameters(
                                parameterWithName("recruitId").description("리크루트 아이디")
                        ),
                        getEnvelopPatternWithNoContent()));
    }

    @DisplayName("리크루트 상세 조회")
    @Test
    void getRecruitDetail() {
        doReturn(RecruitFixture.RECRUIT_1_DETAIL_RES_DTO)
                .when(recruitService)
                .getRecruitDetail(any(), any());

        restDocs
                .when().get("/recruits/{recruitId}", 1)
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .apply(document("recruit/detail",
                        requestCookieAccessTokenOptional(),
                        pathParameters(
                            parameterWithName("recruitId").description("리크루트 아이디")
                        ),
                        getEnvelopPatternWithData().andWithPrefix("data.",
                                fieldWithPath("category").type(JsonFieldType.STRING).description("PROJECT | STUDY"),
                                fieldWithPath("recruitId").type(JsonFieldType.NUMBER).description("리크루트 id"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("리크루트 모집글 제목, 글자 수 제한 50자"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("리크루트 본문, 글자 수 제한 3000자, html 포함 4000자"),
                                fieldWithPath("contactURI").type(JsonFieldType.STRING).description("리크루트 작성자와 연락 가능한 오픈톡 링크"),
                                fieldWithPath("view").type(JsonFieldType.NUMBER).description("리크루트 상세 조회 수"),
                                fieldWithPath("finishedRecruit").type(JsonFieldType.BOOLEAN).description("리크루트 종료 여부"),
                                fieldWithPath("recruitStart").type(JsonFieldType.STRING).description("yyyy-MM-dd 모집 시작 일자"),
                                fieldWithPath("recruitEnd").type(JsonFieldType.STRING).description("yyyy-MM-dd 모집 종료 일자"),
                                fieldWithPath("skills[].skillId").type(JsonFieldType.NUMBER).description("스킬 id (미사용)"),
                                fieldWithPath("skills[].name").type(JsonFieldType.STRING).description("스킬명,  메타데이터-스킬 목록 조회 참고"),
                                fieldWithPath("limits[].recruitType").type(JsonFieldType.STRING).description("리크루트 모집 타입, 메타데이터-리크루트 목록 조회 참고"),
                                fieldWithPath("limits[].limit").type(JsonFieldType.NUMBER).description("리크루트 모집제한 인원"),
                                fieldWithPath("limits[].currentNumber").type(JsonFieldType.NUMBER).description("리크루트 모집인원"),
                                fieldWithPath("author.memberId").type(JsonFieldType.NUMBER).description("리크루트 등록자 PK"),
                                fieldWithPath("author.nickname").type(JsonFieldType.STRING).description("리크루트 등록자 닉네임"),
                                fieldWithPath("author.isMajor").type(JsonFieldType.BOOLEAN).description("리크루트 등록자 전공자 여부"),
                                fieldWithPath("author.ssafyInfo").type(JsonFieldType.OBJECT).optional().description("싸피 정보 여부, null 또는 object"),
                                fieldWithPath("author.ssafyInfo.semester").type(JsonFieldType.NUMBER).description("리크루트 등록자 SSAFY 기수 1이상 10이하"),
                                fieldWithPath("author.ssafyInfo.campus").type(JsonFieldType.STRING).description("리크루트 등록자 소속 캠퍼스, 메타데이터-캠퍼스 목록 조회 참고"),
                                fieldWithPath("author.ssafyInfo.certificationState").type(JsonFieldType.STRING).description("리크루트 등록자 SSAFY 인증 여부"),
                                fieldWithPath("author.ssafyInfo.majorTrack").type(JsonFieldType.STRING).description("리크루트 등록자 전공 트랙 Embedded | Mobile | Python | Java"),
                                fieldWithPath("author.memberRole").type(JsonFieldType.STRING).description("사용자 권한"),
                                fieldWithPath("author.ssafyMember").type(JsonFieldType.BOOLEAN).description("싸피 멤버 여부"),
                                fieldWithPath("questions").type(JsonFieldType.ARRAY).description("리쿠르트 질문"),
                                fieldWithPath("scraped").type(JsonFieldType.BOOLEAN).description("조회자 리크루트 스크랩 여부"),
                                fieldWithPath("mine").type(JsonFieldType.BOOLEAN).description("내가 작성한 리크루트 여부"),
                                fieldWithPath("scrapCount").type(JsonFieldType.NUMBER).description("리쿠르트 스크랩 갯수"),
                                fieldWithPath("matchStatus").type(JsonFieldType.STRING).description("조회 유저 신청 상태")
                        ))
                );
    }

    @DisplayName("리크루트 업데이트")
    @Test
    void updateRecruit() {
        restDocs
                .cookie(ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(RecruitFixture.RECRUIT_1_PATCH_REQ_DTO)
                .when().patch("/recruits/{recruitId}", 1L)
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .apply(document("recruit/updateRecruit",
                        requestCookieAccessTokenMandatory(),
                        pathParameters(
                                parameterWithName("recruitId").description("리크루트 아이디")
                        ),
                        requestFields(
                                fieldWithPath("category").type(JsonFieldType.STRING).description("리크루트 카테고리 : PROJECT | STUDY"),
                                fieldWithPath("registerRecruitType").type(JsonFieldType.STRING).description("리크루트 등록자 리크루트 모집 타입, 메타데이터-리크루트 목록 조회 참고"),
                                fieldWithPath("recruitEnd").type(JsonFieldType.STRING).description("yyyy-MM-dd 모집 종료 일자, 당일 날짜 이후의 값으로만 설정 가능"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("리크루트 모집글 제목, 글자 수 제한 50자"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("리크루트 본문, 글자 수 제한 3000자, html 포함 4000자"),
                                fieldWithPath("contactURI").type(JsonFieldType.STRING).description("리크루트 작성자와 연락 가능한 오픈톡 링크"),
                                fieldWithPath("skills[]").type(JsonFieldType.ARRAY).optional().description("리크루트와 연관된 기술 스택, 메타데이터-스킬 목록 조회 참고"),
                                fieldWithPath("limitations[].recruitType").type(JsonFieldType.STRING).description("리크루트 모집파트, 메타데이터-리크루트 목록 조회 참고"),
                                fieldWithPath("limitations[].limit").type(JsonFieldType.NUMBER).description("인원 제한 1명이상 10명 이하")
                        ),
                        getEnvelopPatternWithNoContent()));
    }

    @DisplayName("리크루트 삭제")
    @Test
    void deleteRecruit() {
        restDocs
                .cookie(ACCESS_TOKEN)
                .when().delete("/recruits/{recruitId}", 1L)
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .apply(document("recruit/deleteRecruit",
                        requestCookieAccessTokenMandatory(),
                        pathParameters(
                                parameterWithName("recruitId").description("리크루트 아이디")
                        ),
                        getEnvelopPatternWithNoContent()));
    }

    @DisplayName("커서 기반 리크루트 목록 조회")
    @Test
    void getRecruitsByCursor() {
        doReturn(RecruitFixture.GET_RECRUITS_CURSOR_RES_DTO)
                .when(recruitService)
                .getRecruitsByCursor(any(), any(), any());

        restDocs
                .cookie(ACCESS_TOKEN)
                .when().get("/recruits/cursor?size=20&category=project&keyword=사이드&isFinished=false&recruitTypes=백엔드&recruitTypes=프론트엔드&skills=Spring&skills=React")
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .apply(document("recruit/recruits-cursor",
                        requestCookieAccessTokenOptional(),
                        requestParameters(
                                parameterWithName("next").optional().description("조회할 커서 번호 default(초기화면)에서는 미포함"),
                                parameterWithName("size").description("페이징 사이즈"),
                                parameterWithName("category").description("카테고리 project|study"),
                                parameterWithName("keyword").description("리크루트 게시글 제목 검색 키워드"),
                                parameterWithName("isFinished").description("리크루트 종료 여부"),
                                parameterWithName("recruitTypes").description("리크루트 모집파트, 메타데이터-리크루트 목록 조회 참고"),
                                parameterWithName("skills").description("리크루트와 연관된 기술 스택, 메타데이터-스킬 목록 조회 참고")
                        ),
                        getEnvelopPatternWithData().andWithPrefix("data.",
                                fieldWithPath("nextCursor").type(JsonFieldType.NUMBER).description("다음 조회할 커서 번호"),
                                fieldWithPath("isLast").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부")
                            ).andWithPrefix("data.recruits[].",
                                fieldWithPath("recruitId").type(JsonFieldType.NUMBER).description("리크루트 id"),
                                fieldWithPath("category").type(JsonFieldType.STRING).description("카테고리 project|study"),
                                fieldWithPath("mine").type(JsonFieldType.BOOLEAN).description("내가 쓴 글 여부(토큰 기준)"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("리크루트 모집글 제목, 글자 수 제한 50자"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("리크루트 본문 요약본 최대 50자"),
                                fieldWithPath("recruitEnd").type(JsonFieldType.STRING).description("yyyy-MM-dd 모집 종료 일자"),
                                fieldWithPath("finishedRecruit").type(JsonFieldType.BOOLEAN).description("리크루트 종료 여부"),
                                fieldWithPath("participants[].members[]").type(JsonFieldType.ARRAY).description("리크루트 참여 멤버 ").optional()
                            ).andWithPrefix("data.recruits[].skills[].",
                                fieldWithPath("skillId").type(JsonFieldType.NUMBER).description("스킬 id 미사용"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("리크루트와 연관된 기술 스택명, 메타데이터-스킬 목록 조회 참고")
                            ).andWithPrefix("data.recruits[].participants[].",
                                fieldWithPath("recruitType").type(JsonFieldType.STRING).description("리크루트 모집파트, 메타데이터-리크루트 목록 조회 참고"),
                                fieldWithPath("limit").type(JsonFieldType.NUMBER).description("리크루트 모집 인원 제한 1명이상 10명 이하")
                            ).andWithPrefix("data.recruits[].participants[].members[].",
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("리크루트 참여자 닉네임"),
                                fieldWithPath("isMajor").type(JsonFieldType.BOOLEAN).description("리크루트 참여자 전공 여부")
                            )
                        )
                );
    }

    @DisplayName("offset 기반 리크루트 목록 조회")
    @Test
    void getRecruitsByOffset() {
        doReturn(RecruitFixture.GET_RECRUITS_OFFSET_RES_DTO)
                .when(recruitService)
                .getRecruitsByOffset(any(), any(), any());

        restDocs
                .cookie(ACCESS_TOKEN)
                .when().get("/recruits/offset?size=20&category=project&keyword=사이드&isFinished=false&recruitTypes=백엔드&recruitTypes=프론트엔드&skills=Spring&skills=React")
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .apply(document("recruit/recruits-page",
                                requestCookieAccessTokenOptional(),
                                requestParameters(
                                        parameterWithName("next").optional().description("조회할 page 번호"),
                                        parameterWithName("size").description("페이징 사이즈"),
                                        parameterWithName("category").description("카테고리 project|study"),
                                        parameterWithName("keyword").description("리크루트 게시글 제목 검색 키워드"),
                                        parameterWithName("isFinished").description("리크루트 종료 여부"),
                                        parameterWithName("recruitTypes").description("리크루트 모집파트, 메타데이터-리크루트 목록 조회 참고"),
                                        parameterWithName("skills").description("리크루트와 연관된 기술 스택, 메타데이터-스킬 목록 조회 참고")
                                ),
                                getEnvelopPatternWithData().andWithPrefix("data.",
                                        fieldWithPath("currentPage").type(JsonFieldType.NUMBER).description("현재 조회한 페이지 번호"),
                                        fieldWithPath("totalPageCount").type(JsonFieldType.NUMBER).description("total 페이지 갯수")
                                ).andWithPrefix("data.recruits[].",
                                        fieldWithPath("recruitId").type(JsonFieldType.NUMBER).description("리크루트 id"),
                                        fieldWithPath("category").type(JsonFieldType.STRING).description("카테고리 project|study"),
                                        fieldWithPath("mine").type(JsonFieldType.BOOLEAN).description("내가 쓴 글 여부(토큰 기준)"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("리크루트 모집글 제목, 글자 수 제한 50자"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("리크루트 본문 요약본 최대 50자"),
                                        fieldWithPath("recruitEnd").type(JsonFieldType.STRING).description("yyyy-MM-dd 모집 종료 일자"),
                                        fieldWithPath("finishedRecruit").type(JsonFieldType.BOOLEAN).description("리크루트 종료 여부"),
                                        fieldWithPath("participants[].members[]").type(JsonFieldType.ARRAY).description("리크루트 참여 멤버 ").optional()
                                ).andWithPrefix("data.recruits[].skills[].",
                                        fieldWithPath("skillId").type(JsonFieldType.NUMBER).description("스킬 id 미사용"),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("리크루트와 연관된 기술 스택명, 메타데이터-스킬 목록 조회 참고")
                                ).andWithPrefix("data.recruits[].participants[].",
                                        fieldWithPath("recruitType").type(JsonFieldType.STRING).description("리크루트 모집파트, 메타데이터-리크루트 목록 조회 참고"),
                                        fieldWithPath("limit").type(JsonFieldType.NUMBER).description("리크루트 모집 인원 제한 1명이상 10명 이하")
                                ).andWithPrefix("data.recruits[].participants[].members[].",
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("리크루트 참여자 닉네임"),
                                        fieldWithPath("isMajor").type(JsonFieldType.BOOLEAN).description("리크루트 참여자 전공 여부")
                                )
                        )
                );
    }

    @DisplayName("스크랩 된 리크루트 목록 조회")
    @Test
    void getScrapedRecruits() {
        doReturn(RecruitFixture.GET_RECRUITS_CURSOR_RES_DTO)
                .when(recruitService)
                .getScrapedRecruits(any(), any(), any());

        restDocs
                .cookie(ACCESS_TOKEN)
                .when().get("/recruits/my-scrap?size=10")
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .apply(document("recruit/my-scrap",
                        requestCookieAccessTokenMandatory(),
                        requestParameters(
                                parameterWithName("cursor").optional().description("다음 조회 커서 default(초기화면)에서는 미포함"),
                                parameterWithName("size").description("페이징 사이즈")
                        ),
                        getEnvelopPatternWithData().andWithPrefix("data.",
                                fieldWithPath("nextCursor").type(JsonFieldType.NUMBER).description("다음 조회할 커서 번호"),
                                fieldWithPath("isLast").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부")
                        ).andWithPrefix("data.recruits[].",
                                fieldWithPath("recruitId").type(JsonFieldType.NUMBER).description("리크루트 id"),
                                fieldWithPath("category").type(JsonFieldType.STRING).description("카테고리 project|study"),
                                fieldWithPath("mine").type(JsonFieldType.BOOLEAN).description("내가 쓴 글 여부(토큰 기준)"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("리크루트 모집글 제목, 글자 수 제한 50자"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("리크루트 본문 요약본 최대 50자"),
                                fieldWithPath("recruitEnd").type(JsonFieldType.STRING).description("yyyy-MM-dd 모집 종료 일자"),
                                fieldWithPath("finishedRecruit").type(JsonFieldType.BOOLEAN).description("리크루트 종료 여부"),
                                fieldWithPath("participants[].members[]").type(JsonFieldType.ARRAY).description("리크루트 참여 멤버 ").optional()
                        ).andWithPrefix("data.recruits[].skills[].",
                                fieldWithPath("skillId").type(JsonFieldType.NUMBER).description("스킬 id 미사용"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("리크루트와 연관된 기술 스택명, 메타데이터-스킬 목록 조회 참고")
                        ).andWithPrefix("data.recruits[].participants[].",
                                fieldWithPath("recruitType").type(JsonFieldType.STRING).description("리크루트 모집파트, 메타데이터-리크루트 목록 조회 참고"),
                                fieldWithPath("limit").type(JsonFieldType.NUMBER).description("리크루트 모집 인원 제한 1명이상 10명 이하")
                        ).andWithPrefix("data.recruits[].participants[].members[].",
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("리크루트 참여자 닉네임"),
                                fieldWithPath("isMajor").type(JsonFieldType.BOOLEAN).description("리크루트 참여자 전공 여부")
                        )
                )
            );
    }

    @DisplayName("사용자 참여 확정 리크루트 목록 조회")
    @Test
    void getMemberJoinedRecruits() {
        doReturn(RecruitFixture.GET_RECRUITS_CURSOR_RES_DTO)
                .when(recruitService)
                .getMemberJoinRecruits(any(), any());

        restDocs
                .cookie(ACCESS_TOKEN)
                .when().get("/recruits/joined?memberId=1&category=PROJECT&size=10")
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .apply(document("recruit/joined",
                                requestCookieAccessTokenOptional(),
                                requestParameters(
                                        parameterWithName("cursor").optional().description("다음 조회 커서 default(초기화면)에서는 미포함"),
                                        parameterWithName("size").description("페이징 사이즈"),
                                        parameterWithName("category").description("카테고리 project|study"),
                                        parameterWithName("memberId").description("사용자 프로필 - 참여중인 리크루트 목록 조회 시 사용될 사용자의 Id")
                                ),
                                getEnvelopPatternWithData().andWithPrefix("data.",
                                        fieldWithPath("nextCursor").type(JsonFieldType.NUMBER).description("다음 조회할 커서 번호"),
                                        fieldWithPath("isLast").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부")
                                ).andWithPrefix("data.recruits[].",
                                        fieldWithPath("recruitId").type(JsonFieldType.NUMBER).description("리크루트 id"),
                                        fieldWithPath("category").type(JsonFieldType.STRING).description("카테고리 project|study"),
                                        fieldWithPath("mine").type(JsonFieldType.BOOLEAN).description("내가 쓴 글 여부(토큰 기준)"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("리크루트 모집글 제목, 글자 수 제한 50자"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("리크루트 본문 요약본 최대 50자"),
                                        fieldWithPath("recruitEnd").type(JsonFieldType.STRING).description("yyyy-MM-dd 모집 종료 일자"),
                                        fieldWithPath("finishedRecruit").type(JsonFieldType.BOOLEAN).description("리크루트 종료 여부"),
                                        fieldWithPath("participants[].members[]").type(JsonFieldType.ARRAY).description("리크루트 참여 멤버 ").optional()
                                ).andWithPrefix("data.recruits[].skills[].",
                                        fieldWithPath("skillId").type(JsonFieldType.NUMBER).description("스킬 id 미사용"),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("리크루트와 연관된 기술 스택명, 메타데이터-스킬 목록 조회 참고")
                                ).andWithPrefix("data.recruits[].participants[].",
                                        fieldWithPath("recruitType").type(JsonFieldType.STRING).description("리크루트 모집파트, 메타데이터-리크루트 목록 조회 참고"),
                                        fieldWithPath("limit").type(JsonFieldType.NUMBER).description("리크루트 모집 인원 제한 1명이상 10명 이하")
                                ).andWithPrefix("data.recruits[].participants[].members[].",
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("리크루트 참여자 닉네임"),
                                        fieldWithPath("isMajor").type(JsonFieldType.BOOLEAN).description("리크루트 참여자 전공 여부")
                                )
                        )
                );
    }

    @DisplayName("내가 신청한 리크루트 상태별 목록 조회")
    @Test
    void getAppiedRecruits() {
        doReturn(RecruitFixture.GET_MEMBER_APPLIED_RECRUITS_RES_DTO)
                .when(recruitService)
                .getMemberAppliedRecruits(any(), any());

        restDocs
                .cookie(ACCESS_TOKEN)
                .when().get("/recruits/applied?size=10&category=project")
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .apply(document("recruit/applied-recruits",
                                requestCookieAccessTokenMandatory(),
                                requestParameters(
                                        parameterWithName("cursor").optional().description("다음 조회 커서 default(초기화면)에서는 미포함"),
                                        parameterWithName("size").description("페이징 사이즈"),
                                        parameterWithName("category").description("카테고리 project|study"),
                                        parameterWithName("matchStatus").optional().description("리크루트 매칭 상태")
                                ),
                                getEnvelopPatternWithData().andWithPrefix("data.",
                                        fieldWithPath("nextCursor").type(JsonFieldType.NUMBER).description("다음 조회할 커서 번호"),
                                        fieldWithPath("isLast").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부")
                                ).andWithPrefix("data.recruits[].",
                                        fieldWithPath("recruitId").type(JsonFieldType.NUMBER).description("리크루트 id"),
                                        fieldWithPath("category").type(JsonFieldType.STRING).description("카테고리 project|study"),
                                        fieldWithPath("mine").type(JsonFieldType.BOOLEAN).description("내가 쓴 글 여부(토큰 기준)"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("리크루트 모집글 제목, 글자 수 제한 50자"),
                                        fieldWithPath("matchStatus").type(JsonFieldType.STRING).description("사용자 리크루트 매칭 상태"),
                                        fieldWithPath("appliedAt").type(JsonFieldType.STRING).description("사용자 리크루트 신청일"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("리크루트 본문 요약본 최대 50자"),
                                        fieldWithPath("recruitEnd").type(JsonFieldType.STRING).description("yyyy-MM-dd 모집 종료 일자"),
                                        fieldWithPath("finishedRecruit").type(JsonFieldType.BOOLEAN).description("리크루트 종료 여부"),
                                        fieldWithPath("participants[].members[]").type(JsonFieldType.ARRAY).description("리크루트 참여 멤버 ").optional()
                                ).andWithPrefix("data.recruits[].skills[].",
                                        fieldWithPath("skillId").type(JsonFieldType.NUMBER).description("스킬 id 미사용"),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("리크루트와 연관된 기술 스택명, 메타데이터-스킬 목록 조회 참고")
                                ).andWithPrefix("data.recruits[].participants[].",
                                        fieldWithPath("recruitType").type(JsonFieldType.STRING).description("리크루트 모집파트, 메타데이터-리크루트 목록 조회 참고"),
                                        fieldWithPath("limit").type(JsonFieldType.NUMBER).description("리크루트 모집 인원 제한 1명이상 10명 이하")
                                ).andWithPrefix("data.recruits[].participants[].members[].",
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("리크루트 참여자 닉네임"),
                                        fieldWithPath("isMajor").type(JsonFieldType.BOOLEAN).description("리크루트 참여자 전공 여부")
                                )
                        )
                );
    }
}
