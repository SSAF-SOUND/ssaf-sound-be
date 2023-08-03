package com.ssafy.ssafsound.domain.board.controller;

import com.ssafy.ssafsound.domain.board.domain.Board;
import com.ssafy.ssafsound.domain.board.dto.GetBoardResDto;
import com.ssafy.ssafsound.domain.board.service.BoardService;
import com.ssafy.ssafsound.global.config.RestDocsConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@Import(RestDocsConfiguration.class)
class BoardControllerTest {
    @Autowired
    private RestDocumentationResultHandler restDocs;

    private MockMvc mockMvc;

    private Cookie cookie;

    @MockBean
    private BoardService boardService;

    @BeforeEach
    void setUp(WebApplicationContext context, RestDocumentationContextProvider provider) {
        this.cookie = new Cookie("accessToken", "{accessToken}refreshToken={refreshToken}");
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                .alwaysDo(MockMvcResultHandlers.print())
                .alwaysDo(restDocs)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @Test
    public void findBoards() throws Exception {
        // generate data
        List<Board> boards = new ArrayList<>();
        Board board1 = Board.builder()
                .id(1L)
                .title("자유 게시판")
                .usedBoard(true)
                .build();
        boards.add(board1);

        Board board2 = Board.builder()
                .id(2L)
                .title("취업 게시판")
                .usedBoard(true)
                .build();
        boards.add(board2);

        // given
        given(boardService.findBoards()).willReturn(GetBoardResDto.from(boards));

        // Controller Test & Generate Rest docs
        mockMvc.perform(get("/boards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(cookie)
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
                                        fieldWithPath("boardId").description("해당 게시판의 id"),
                                        fieldWithPath("title").description("게시판 이름"),
                                        fieldWithPath("usedBoard").description("게시판 사용여부")
                                )
                        )
                );
    }
}