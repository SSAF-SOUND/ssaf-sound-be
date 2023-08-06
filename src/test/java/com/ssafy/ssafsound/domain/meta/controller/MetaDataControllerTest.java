package com.ssafy.ssafsound.domain.meta.controller;

import com.ssafy.ssafsound.utils.BaseRestDocControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MetaDataControllerTest extends BaseRestDocControllerTest {

    @Test
    @DisplayName("캠퍼스 종류 조회 ex) 서울, 대구")
    public void findCampuses() throws Exception {
        this.mockMvc.perform(get("/meta/campuses")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                responseFields(
                                        fieldWithPath("code").description("응답 코드"),
                                        fieldWithPath("message").description("응답 메시지"),
                                        fieldWithPath("data").description("응답 데이터"),
                                        fieldWithPath("data.campuses").description("캠퍼스 목록")
                                ).andWithPrefix("data.campuses[].",
                                        fieldWithPath("id").description("캠퍼스 id (미사용)"),
                                        fieldWithPath("name").description("캠퍼스 이름")
                                )
                        )
                );
    }

    @Test
    @DisplayName("기술 스택 조회 ex) Spring, React .. ")
    public void findSkills() throws Exception {
        this.mockMvc.perform(get("/meta/skills")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                responseFields(
                                        fieldWithPath("code").description("응답 코드"),
                                        fieldWithPath("message").description("응답 메시지"),
                                        fieldWithPath("data").description("응답 데이터"),
                                        fieldWithPath("data.skills").description("기술 스택 목록")
                                ).andWithPrefix("data.skills[].",
                                        fieldWithPath("id").description("기술 id (미사용)"),
                                        fieldWithPath("name").description("기술 이름")
                                )
                        )
                );
    }

    @Test
    @DisplayName("리크루트 모집 인원 타입 조회 ex) 백엔드, 프론트엔드")
    public void findRecruitTypes() throws Exception {
        this.mockMvc.perform(get("/meta/recruit-types")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(
                restDocs.document(
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data").description("응답 데이터"),
                                fieldWithPath("data.recruitTypes").description("리크루트 모집 인원 타입")
                        ).andWithPrefix("data.recruitTypes[].",
                                fieldWithPath("id").description("리크루트 모집타입 id (미사용)"),
                                fieldWithPath("name").description("리크루트 모집타입명")
                        )
                )
        );
    }
}
