package com.ssafy.ssafsound.domain.board.controller;

import com.ssafy.ssafsound.domain.board.dto.GetBoardResDto;
import com.ssafy.ssafsound.global.docs.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BoardControllerTest extends ControllerTest {
    @Test
    @DisplayName("게시판 목록 조회 데이터 반환")
    public void findBoards() {
//        doReturn(GetBoardResDto.of)
//                .when(boardService)
//                .findBoards();

        restDocs
                .when().get("/boards")
                .then().log().all()
                .apply(document("board/find-boards"))
                .statusCode(HttpStatus.OK.value());

//        mockMvc.perform(get("/boards")
//                        .contentType(MediaType.APPLICATION_JSON)
//                )
//                .andExpect(status().isOk())
//                .andDo(
//                        restDocs.document(
//                                responseFields(
//                                        fieldWithPath("code").description("응답 코드"),
//                                        fieldWithPath("message").description("응답 메시지"),
//                                        fieldWithPath("data").description("응답 데이터"),
//                                        fieldWithPath("data.boards").description("게시판 목록 데이터")
//                                ).andWithPrefix("data.boards[].",
//                                        fieldWithPath("boardId").description("게시판의 id"),
//                                        fieldWithPath("title").description("게시판 이름"),
//                                        fieldWithPath("imageUrl").description("게시판 이미지"),
//                                        fieldWithPath("description").description("게시판 설명")
//                                )
//                        )
//                );
    }
}