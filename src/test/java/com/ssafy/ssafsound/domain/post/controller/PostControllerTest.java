package com.ssafy.ssafsound.domain.post.controller;

import com.ssafy.ssafsound.global.docs.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.ssafy.ssafsound.global.docs.snippet.CookieDescriptionSnippet.requestCookieAccessTokenNeedless;
import static com.ssafy.ssafsound.global.docs.snippet.CookieDescriptionSnippet.requestCookieAccessTokenOptional;
import static com.ssafy.ssafsound.global.util.fixture.PostFixture.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;

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
                        requestCookieAccessTokenNeedless(),
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
    @DisplayName("게시글 상세보기 조회(익명X)")
    void findPost_Anonymity_False() {
        doReturn(GET_POST_DETAIL_RES_DTO1)
                .when(postService)
                .findPost(any(), any());

        restDocs.cookie(ACCESS_TOKEN)
                .when().get("/posts/{postId}", 2L)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .apply(document("post/find-post",
                        requestCookieAccessTokenOptional(),
                        pathParameters(
                                parameterWithName("postId").description("조회할 게시글의 고유 ID")
                        ),
                        getEnvelopPatternWithData().andWithPrefix("data.",
                                fieldWithPath("post").type(JsonFieldType.OBJECT).description("게시글 정보"),
                                fieldWithPath("author").type(JsonFieldType.OBJECT).description("게시글 작성자의 정보")
                        ).andWithPrefix("data.post.",
                                fieldWithPath("boardId").type(JsonFieldType.NUMBER).description("게시글이 작성된 게시판 종류의 ID"),
                                fieldWithPath("boardTitle").type(JsonFieldType.STRING).description("게시글이 작성된 게시판의 종류, 자유 게시판 | 취업 게시판 | 맛집 게시판 | 질문 게시판 | 싸피 예비생 게시판"),
                                fieldWithPath("postId").type(JsonFieldType.NUMBER).description("게시글의 고유 ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("게시글의 제목, 길이제한 2 ~ 100"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("게시글의 내용, 최소길이 2"),
                                fieldWithPath("likeCount").type(JsonFieldType.NUMBER).description("게시글의 좋아요 개수"),
                                fieldWithPath("commentCount").type(JsonFieldType.NUMBER).description("게시글의 댓글 개수"),
                                fieldWithPath("scrapCount").type(JsonFieldType.NUMBER).description("게시글의 스크랩 개수"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("게시글의 작성일").optional(),
                                fieldWithPath("anonymity").type(JsonFieldType.BOOLEAN).description("게시글 작성자의 익명 여부인지 나타내는 필드"),
                                fieldWithPath("modified").type(JsonFieldType.BOOLEAN).description("게시글이 수정되었는지 여부를 나타내는 필드"),
                                fieldWithPath("scraped").type(JsonFieldType.BOOLEAN).description("로그인한 사용자라면 자신이 해당 게시글을 스크랩 했는지 여부를 나타내는 필드"),
                                fieldWithPath("liked").type(JsonFieldType.BOOLEAN).description("로그인한 사용자라면 자신이 해당 게시글을 좋아요 했는지 여부를 나타내는 필드"),
                                fieldWithPath("mine").type(JsonFieldType.BOOLEAN).description("로그인한 사용자라면 이 게시글이 자신이 작성한 게시글인지 여부를 나타내는 필드"),
                                fieldWithPath("images").type(JsonFieldType.ARRAY).description("게시글의 이미지 정보")
                        ).andWithPrefix("data.post.images[].",
                                fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("게시글의 이미지 URL, S3에 저장된 CDN 주소.").optional()
                        ).andWithPrefix("data.author.",
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("게시글 작성자의 닉네임"),
                                fieldWithPath("memberRole").type(JsonFieldType.STRING).description("게시글 작성자의 사용자 권한").optional(),
                                fieldWithPath("ssafyMember").type(JsonFieldType.BOOLEAN).description("게시글 작성자가 SSAFY 유저인지 나타내는 필드 값").optional(),
                                fieldWithPath("isMajor").type(JsonFieldType.BOOLEAN).description("게시글 작성자가 전공자인지 여부, true면 전공자, false면 비전공자").optional(),
                                fieldWithPath("ssafyInfo").type(JsonFieldType.OBJECT).description("게시글 작성자가 선택한 SSAFY 정보 단, SSAFY 인증을 받지 않아도, 해당 필드는 존재하지만 익명이면 null값이 들어감.").optional()
                        ).andWithPrefix("data.author.ssafyInfo.",
                                fieldWithPath("semester").type(JsonFieldType.NUMBER).description("작성자의 SSAFY 기수를 의미"),
                                fieldWithPath("campus").type(JsonFieldType.STRING).description("작성자의 SSAFY 캠퍼스를 의미"),
                                fieldWithPath("certificationState").type(JsonFieldType.STRING).description("작성자의 SSAFY 인증 여부를 의미함, CERTIFIED | UNCERTIFIED"),
                                fieldWithPath("majorTrack").type(JsonFieldType.STRING).description("작성자의 전공트랙 정보를 의미, Java | Python | Mobile | Embedded")
                        )
                ));
    }

    @Test
    void likePost() {
    }

    @Test
    void scrapPost() {
    }

//    @Test
//    void reportPost() {
//    }

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