package com.ssafy.ssafsound.domain.board.controller;

import com.ssafy.ssafsound.domain.board.domain.Board;
import com.ssafy.ssafsound.domain.board.dto.GetBoardResDto;
import com.ssafy.ssafsound.domain.board.service.BoardService;
import com.ssafy.ssafsound.global.docs.RestDocsTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BoardController.class)
class BoardControllerTest extends RestDocsTest {

    @MockBean
    private BoardService boardService;

    @Test
    public void findBoards() throws Exception {
        // generate data
        List<Board> boards = new ArrayList<>();
        Board board1 = Board.builder()
                .id(1L)
                .title("자유 게시판")
                .usedBoard(true)
                .imageUrl("testUrl")
                .description("test 설명1")
                .build();
        boards.add(board1);

        Board board2 = Board.builder()
                .id(2L)
                .title("취업 게시판")
                .usedBoard(true)
                .imageUrl("testUrl")
                .description("test 설명2")
                .build();
        boards.add(board2);

        given(boardService.findBoards()).willReturn(GetBoardResDto.from(boards));
        Cookie cookie = new Cookie("accessToken", "{accessToken}refreshToken={refreshToken}");

        mockMvc.perform(get("/boards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(cookie)
                )
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                responseFields(
                                        fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                        fieldWithPath("data.boards").type(JsonFieldType.ARRAY).description("게시판 목록 데이터")
                                ).andWithPrefix("data.boards[].",
                                        fieldWithPath("boardId").type(JsonFieldType.NUMBER).description("게시판의 id"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("게시판 이름"),
                                        fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("이미지 URL"),
                                        fieldWithPath("description").type(JsonFieldType.STRING).description("설명")
                                )
                        )
                );
    }
}