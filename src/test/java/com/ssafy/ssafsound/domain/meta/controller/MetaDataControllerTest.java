package com.ssafy.ssafsound.domain.meta.controller;

import com.ssafy.ssafsound.domain.meta.domain.*;
import com.ssafy.ssafsound.domain.meta.service.EnumMetaDataConsumer;
import com.ssafy.ssafsound.global.docs.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MetaDataController.class)
public class MetaDataControllerTest extends ControllerTest {

    @MockBean
    EnumMetaDataConsumer consumer;

    @Test
    @DisplayName("캠퍼스 종류 조회 ex) 서울, 대구")
    public void findCampuses() throws Exception {

        given(consumer.getMetaDataList(MetaDataType.CAMPUS.name()))
                .willReturn(List.of( new MetaData(Campus.SEOUL)));

        this.mockMvc.perform(get("/meta/campuses")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                responseFields(
                                        fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                        fieldWithPath("data.campuses").type(JsonFieldType.ARRAY).description("캠퍼스 목록")
                                ).andWithPrefix("data.campuses[].",
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("캠퍼스 id (미사용)"),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("캠퍼스 이름")
                                )
                        )
                );
    }

    @Test
    @DisplayName("기술 스택 조회 ex) Spring, React .. ")
    public void findSkills() throws Exception {

        given(consumer.getMetaDataList(MetaDataType.SKILL.name()))
                .willReturn(List.of( new MetaData(Skill.JAVA)));

        this.mockMvc.perform(get("/meta/skills")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                responseFields(
                                        fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                        fieldWithPath("data.skills").type(JsonFieldType.ARRAY).description("기술 스택 목록")
                                ).andWithPrefix("data.skills[].",
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("기술 id (미사용)"),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("기술 이름")
                                )
                        )
                );
    }

    @Test
    @DisplayName("리크루트 모집 인원 타입 조회 ex) 백엔드, 프론트엔드")
    public void findRecruitTypes() throws Exception {

        given(consumer.getMetaDataList(MetaDataType.RECRUIT_TYPE.name()))
                .willReturn(List.of( new MetaData(RecruitType.BACK_END)));

        this.mockMvc.perform(get("/meta/recruit-types")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(
                restDocs.document(
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                fieldWithPath("data.recruitTypes").type(JsonFieldType.ARRAY).description("리크루트 모집 인원 타입")
                        ).andWithPrefix("data.recruitTypes[].",
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("리크루트 모집타입 id (미사용)"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("리크루트 모집타입명")
                        )
                )
        );
    }
}
