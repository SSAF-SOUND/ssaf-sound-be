package com.ssafy.ssafsound.domain.board.controller;

import com.ssafy.ssafsound.global.docs.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.ssafy.ssafsound.global.docs.snippet.CookieDescriptionSnippet.requestCookieAccessTokenOptional;
import static com.ssafy.ssafsound.global.util.fixture.BoardFixture.GET_BOARD_RES_DTO1;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

class BoardControllerTest extends ControllerTest {
    @Test
    @DisplayName("게시판 목록 조회 데이터 반환")
    public void findBoards() {
        doReturn(GET_BOARD_RES_DTO1)
                .when(boardService)
                .findBoards();

        restDocs.cookie(ACCESS_TOKEN)
                .when().get("/boards")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .apply(document("board/find-boards",
                        requestCookieAccessTokenOptional(),
                        getEnvelopPatternWithData().andWithPrefix("data.",
                                fieldWithPath("boards").type(JsonFieldType.ARRAY).description("게시판 목록")
                        ).andWithPrefix("data.boards[].",
                                fieldWithPath("boardId").type(JsonFieldType.NUMBER).description("게시판 목록의 ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("게시판 목록의 제목, 자유 게시판 | 취업 게시판 | 맛집 게시판 | 질문 게시판 | 싸피 예비생 게시판"),
                                fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("게시판 배너 이미지"),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("게시판의 특성을 설명해주는 설명문")
                        )));
    }
}