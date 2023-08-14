package com.ssafy.ssafsound.domain.post.controller;

import com.ssafy.ssafsound.global.docs.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.ssafy.ssafsound.global.docs.snippet.CookieDescriptionSnippet.requestCookieAccessTokenOptional;
import static com.ssafy.ssafsound.global.util.fixture.BoardFixture.GET_BOARD_RES_DTO1;
import static com.ssafy.ssafsound.global.util.fixture.PostFixture.GET_POST_RES_DTO1;
import static com.ssafy.ssafsound.global.util.fixture.PostFixture.PAGE_SIZE1;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

class PostControllerTest extends ControllerTest {

    @Test
    @DisplayName("게시글 목록 조회, cursor와 size를 기준으로 커서기반 페이지네이션이 수행됨.")
    void findPosts() {
        doReturn(GET_POST_RES_DTO1)
                .when(postService)
                .findPosts(any());

        restDocs.cookie(ACCESS_TOKEN)
                .when().get("/posts?boardId=1&cursor=-1&size={PAGE_SIZE1}", PAGE_SIZE1)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .apply(document("post/find-posts",
                        requestCookieAccessTokenOptional(),
                        requestParameters(
                                parameterWithName("boardId").description("조회하려는 게시글의 게시판 고유 ID"),
                                parameterWithName("cursor").description("cursor값은 다음 페이지를 가져올 마지막 페이지 번호를 의미함, 초기 cursor는 -1, 이후 cursor값은 응답 데이터로 제공되는 cursor값을 사용."),
                                parameterWithName("size").description("cursor를 기준으로 다음에 불러올 페이지의 size를 의미, 최소 size는 10")
                        ),
                        getEnvelopPatternWithData().andWithPrefix("data.",
                                fieldWithPath("posts").type(JsonFieldType.ARRAY).description("게시글 목록"),
                                fieldWithPath("cursor").type(JsonFieldType.NUMBER).description("다음에 요청할 cursor값, 응답되는 cursor값이 null이면 다음 페이지는 없음을 의미").optional()
                        ).andWithPrefix("data.posts[].",
                                fieldWithPath("boardId").type(JsonFieldType.NUMBER).description("게시글이 작성된 게시판 종류의 ID"),
                                fieldWithPath("boardTitle").type(JsonFieldType.STRING).description("게시글이 작성된 게시판의 종류, 자유 게시판 | 취업 게시판 | 맛집 게시판 | 질문 게시판 | 싸피 예비생 게시판"),
                                fieldWithPath("postId").type(JsonFieldType.NUMBER).description("게시글의 고유 ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("게시글의 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("게시글의 내용"),
                                fieldWithPath("likeCount").type(JsonFieldType.NUMBER).description("게시글의 좋아요 개수"),
                                fieldWithPath("commentCount").type(JsonFieldType.NUMBER).description("게시글의 댓글 개수"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("게시글의 작성일").optional(),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("게시글 작성자의 닉네임"),
                                fieldWithPath("anonymity").type(JsonFieldType.BOOLEAN).description("게시글 작성자의 익명 여부인지 나타내는 필드"),
                                fieldWithPath("thumbnail").type(JsonFieldType.STRING).description("게시판의 썸네일, 게시글의 사진이 여러개가 있을 때 첫 번째 사진이 해당 게시글의 썸네일이 됨.").optional()
                        )));
    }

    @Test
    void findPost() {
    }

    @Test
    void likePost() {
    }

    @Test
    void scrapPost() {
    }

    @Test
    void reportPost() {
    }

    @Test
    void writePost() {
    }

    @Test
    void deletePost() {
    }

    @Test
    void updatePost() {
    }

    @Test
    void findHotPosts() {
    }

    @Test
    void findMyPosts() {
    }

    @Test
    void searchPosts() {
    }

    @Test
    void searchHotPosts() {
    }
}