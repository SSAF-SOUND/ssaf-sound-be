package com.ssafy.ssafsound.domain.board.controller;

import com.ssafy.ssafsound.utils.BaseRestDocControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BoardControllerTest extends BaseRestDocControllerTest {
    @Test
    @DisplayName("게시판 조회")
    public void findBoards() throws Exception {
        mockMvc.perform(get("/boards")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                responseFields(
                                        fieldWithPath("code").description("응답 코드"),
                                        fieldWithPath("message").description("응답 메시지"),
                                        fieldWithPath("data").description("응답 데이터"),
                                        fieldWithPath("data.boards").description("게시판 목록 데이터")
                                ).andWithPrefix("data.boards[].",
                                        fieldWithPath("boardId").description("게시판의 id"),
                                        fieldWithPath("title").description("게시판 이름"),
                                        fieldWithPath("imageUrl").description("게시판 이미지"),
                                        fieldWithPath("description").description("게시판 설명")
                                )
                        )
                );
    }
}