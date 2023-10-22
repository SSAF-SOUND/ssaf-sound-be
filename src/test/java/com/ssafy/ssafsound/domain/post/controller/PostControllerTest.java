package com.ssafy.ssafsound.domain.post.controller;

import com.ssafy.ssafsound.global.docs.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.ssafy.ssafsound.global.docs.snippet.CookieDescriptionSnippet.*;
import static com.ssafy.ssafsound.global.util.fixture.PostFixture.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

class PostControllerTest extends ControllerTest {

    @Test
    @DisplayName("게시글 목록 조회(Cursor), cursor와 size를 기준으로 커서 기반 페이지네이션이 수행됨.")
    void findPostsByCursor() {
        doReturn(GET_POST_CURSOR_RES_DTO3)
                .when(postService)
                .findPostsByCursor(any());

        restDocs.cookie(ACCESS_TOKEN)
                .when().get("/posts/cursor?boardId={boardId}&cursor={cursor}&size={pageSize}", 1, -1, 10)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .apply(document("post/find-posts-by-cursor",
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
                                )
                        )
                );
    }

    @Test
    @DisplayName("게시글 목록 조회(Offset), page와 size를 기준으로 오프셋 기반 페이지네이션이 수행됨.")
    void findPostsByOffset() {
        doReturn(GET_POST_OFFSET_RES_DTO3)
                .when(postService)
                .findPostsByOffset(any());

        restDocs.cookie(ACCESS_TOKEN)
                .when().get("/posts/offset?boardId={boardId}&page={page}&size={pageSize}", 1, 1, 10)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .apply(document("post/find-posts-by-offset",
                                requestCookieAccessTokenNeedless(),
                                requestParameters(
                                        parameterWithName("boardId").description("조회하려는 게시글의 게시판 고유 ID"),
                                        parameterWithName("page").description("page값은 불러올 현재 페이지의 값을 의미함, 초기 page는 1(또는 첫 페이지는 1)"),
                                        parameterWithName("size").description("현재 페이지의 게시글 개수를 의미함, 최소 size는 10")
                                ),
                                getEnvelopPatternWithData().andWithPrefix("data.",
                                        fieldWithPath("posts").type(JsonFieldType.ARRAY).description("게시글 목록")
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
                                )
                        )
                );
    }

    @Test
    @DisplayName("게시글 검색(Cursor), 제목 + 내용을 기준으로 검색")
    void searchPostsByCursor() {
        doReturn(GET_POST_CURSOR_RES_DTO4)
                .when(postService)
                .searchPostsByCursor(any());

        restDocs.cookie(ACCESS_TOKEN)
                .when().get("/posts/search/cursor?boardId={boardId}&keyword={searchText}&cursor={cursor}&size={pageSize}", 1L, "안녕하세요", -1, 10)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .apply(document("post/search-posts-by-cursor",
                                requestCookieAccessTokenNeedless(),
                                requestParameters(
                                        parameterWithName("boardId").description("검색하려는 게시판 고유 ID"),
                                        parameterWithName("keyword").description("검색하려는 게시글의 검색어, 최소 2글자"),
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
                                )
                        )
                );
    }

    @Test
    @DisplayName("게시글 상세보기 조회(익명X)")
    void findPost_Anonymity_False() {
        doReturn(GET_POST_DETAIL_RES_DTO1)
                .when(postService)
                .findPost(any(), any());

        restDocs.cookie(ACCESS_TOKEN)
                .when().get("/posts/{postId}", 1L)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .apply(document("post/find-post-anonymity-false",
                                requestCookieAccessTokenOptional(),
                                pathParameters(
                                        parameterWithName("postId").description("조회할 게시글의 고유 ID")
                                ),
                                getEnvelopPatternWithData().andWithPrefix("data.",
                                        fieldWithPath("post").type(JsonFieldType.OBJECT).description("게시글 정보")
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
                                        fieldWithPath("images").type(JsonFieldType.ARRAY).description("게시글의 이미지 정보"),
                                        fieldWithPath("author").type(JsonFieldType.OBJECT).description("게시글 작성자의 정보")
                                ).andWithPrefix("data.post.images[].",
                                        fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("게시글의 이미지 URL, S3에 저장된 CDN 주소.").optional()
                                ).andWithPrefix("data.post.author.",
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("게시글 작성자의 PK(단, 익명이면 -1)"),
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("게시글 작성자의 닉네임"),
                                        fieldWithPath("memberRole").type(JsonFieldType.STRING).description("게시글 작성자의 사용자 권한").optional(),
                                        fieldWithPath("ssafyMember").type(JsonFieldType.BOOLEAN).description("게시글 작성자가 SSAFY 유저인지 나타내는 필드 값").optional(),
                                        fieldWithPath("isMajor").type(JsonFieldType.BOOLEAN).description("게시글 작성자가 전공자인지 여부, true면 전공자, false면 비전공자").optional(),
                                        fieldWithPath("ssafyInfo").type(JsonFieldType.OBJECT).description("게시글 작성자가 선택한 SSAFY 정보 단, SSAFY 인증을 받지 않아도, 해당 필드는 존재하지만 익명이면 null값이 들어감.").optional()
                                ).andWithPrefix("data.post.author.ssafyInfo.",
                                        fieldWithPath("semester").type(JsonFieldType.NUMBER).description("작성자의 SSAFY 기수를 의미"),
                                        fieldWithPath("campus").type(JsonFieldType.STRING).description("작성자의 SSAFY 캠퍼스를 의미"),
                                        fieldWithPath("certificationState").type(JsonFieldType.STRING).description("작성자의 SSAFY 인증 여부를 의미함, CERTIFIED | UNCERTIFIED"),
                                        fieldWithPath("majorTrack").type(JsonFieldType.STRING).description("작성자의 전공트랙 정보를 의미, Java | Python | Mobile | Embedded")
                                )
                        )
                );
    }

    @Test
    @DisplayName("게시글 상세보기 조회(익명O)")
    void findPost_Anonymity_True() {
        doReturn(GET_POST_DETAIL_RES_DTO2)
                .when(postService)
                .findPost(any(), any());

        restDocs.cookie(ACCESS_TOKEN)
                .when().get("/posts/{postId}", 2L)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .apply(document("post/find-post-anonymity-true",
                                requestCookieAccessTokenOptional(),
                                pathParameters(
                                        parameterWithName("postId").description("조회할 게시글의 고유 ID")
                                ),
                                getEnvelopPatternWithData().andWithPrefix("data.",
                                        fieldWithPath("post").type(JsonFieldType.OBJECT).description("게시글 정보")
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
                                        fieldWithPath("images").type(JsonFieldType.ARRAY).description("게시글의 이미지 정보"),
                                        fieldWithPath("author").type(JsonFieldType.OBJECT).description("게시글 작성자의 정보")
                                ).andWithPrefix("data.post.images[].",
                                        fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("게시글의 이미지 URL, S3에 저장된 CDN 주소.").optional()
                                ).andWithPrefix("data.post.author.",
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("게시글 작성자의 PK"),
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("게시글 작성자의 닉네임"),
                                        fieldWithPath("memberRole").type(JsonFieldType.STRING).description("게시글 작성자의 사용자 권한").optional(),
                                        fieldWithPath("ssafyMember").type(JsonFieldType.BOOLEAN).description("게시글 작성자가 SSAFY 유저인지 나타내는 필드 값").optional(),
                                        fieldWithPath("isMajor").type(JsonFieldType.BOOLEAN).description("게시글 작성자가 전공자인지 여부, true면 전공자, false면 비전공자").optional(),
                                        fieldWithPath("ssafyInfo").type(JsonFieldType.OBJECT).description("게시글 작성자가 선택한 SSAFY 정보 단, SSAFY 인증을 받지 않아도, 해당 필드는 존재하지만 익명이면 null값이 들어감.").optional()
                                ).andWithPrefix("data.post.author.ssafyInfo.",
                                        fieldWithPath("semester").type(JsonFieldType.NUMBER).description("작성자의 SSAFY 기수를 의미"),
                                        fieldWithPath("campus").type(JsonFieldType.STRING).description("작성자의 SSAFY 캠퍼스를 의미"),
                                        fieldWithPath("certificationState").type(JsonFieldType.STRING).description("작성자의 SSAFY 인증 여부를 의미함, CERTIFIED | UNCERTIFIED"),
                                        fieldWithPath("majorTrack").type(JsonFieldType.STRING).description("작성자의 전공트랙 정보를 의미, Java | Python | Mobile | Embedded")
                                )
                        )
                );
    }

    @Test
    @DisplayName("게시글 좋아요(등록, 취소가능)")
    void likePost() {
        doReturn(POST_POST_LIKE_RES_DTO)
                .when(postService)
                .likePost(any(), any());

        restDocs.cookie(ACCESS_TOKEN)
                .when().post("/posts/{postId}/like", 1L)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .apply(document("post/like-post",
                                requestCookieAccessTokenMandatory(),
                                pathParameters(
                                        parameterWithName("postId").description("좋아요를 누를 게시글 고유 ID")
                                ),
                                getEnvelopPatternWithData().andWithPrefix("data.",
                                        fieldWithPath("likeCount").type(JsonFieldType.NUMBER).description("좋아요를 클릭 한 후 현재 게시글의 좋아요 개수"),
                                        fieldWithPath("liked").type(JsonFieldType.BOOLEAN).description("해당 게시글의 좋아요를 눌렀는지 여부, true면 좋아요 등록, false면 좋아요 취소")
                                )
                        )
                );
    }

    @Test
    @DisplayName("게시글 스크랩(등록, 취소가능)")
    void scrapPost() {
        doReturn(POST_POST_SCRAP_RES_DTO)
                .when(postService)
                .scrapPost(any(), any());

        restDocs.cookie(ACCESS_TOKEN)
                .when().post("/posts/{postId}/scrap", 1L)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .apply(document("post/scrap-post",
                                requestCookieAccessTokenMandatory(),
                                pathParameters(
                                        parameterWithName("postId").description("스크랩을 누를 게시글 고유 ID")
                                ),
                                getEnvelopPatternWithData().andWithPrefix("data.",
                                        fieldWithPath("scrapCount").type(JsonFieldType.NUMBER).description("스크랩을 클릭 한 후 현재 게시글의 스크랩 개수"),
                                        fieldWithPath("scraped").type(JsonFieldType.BOOLEAN).description("해당 게시글의 스크랩을 눌렀는지 여부, true면 스크랩 등록, false면 스크랩 취소")
                                )
                        )
                );
    }

    @Test
    @DisplayName("게시글 작성, 성공 시 작성한 postId 응답")
    void writePost() {
        doReturn(POST_ID_ELEMENT)
                .when(postService)
                .writePost(any(), any(), any());

        restDocs.cookie(ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(POST_POST_WRITE_REQ_DTO1)
                .when().post("/posts?boardId={boardId}", 1L)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .apply(document("post/write-post",
                                requestCookieAccessTokenMandatory(),
                                requestParameters(
                                        parameterWithName("boardId").description("게시글을 작성할 게시판의 고유 ID")
                                ),
                                requestFields(
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 내용"),
                                        fieldWithPath("anonymity").type(JsonFieldType.BOOLEAN).description("게시글 작성자의 익명 여부를 나타내는 필드"),
                                        fieldWithPath("images").type(JsonFieldType.ARRAY).description("게시글 사진 목록")
                                ).andWithPrefix("images[].",
                                        fieldWithPath("imagePath").type(JsonFieldType.STRING).description("s3에 저장된 경로를 의미하는 필드, s3에 저장된 데이터를 삭제할 때 사용").optional(),
                                        fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("s3에 저장된 이미지를 CDN URL 주소로 가져오기 위한 필드, 해당 URL에 접근만 해도 이미지에 접근할 수 있는 경로를 의미").optional()
                                ),
                                getEnvelopPatternWithData().andWithPrefix("data.",
                                        fieldWithPath("postId").type(JsonFieldType.NUMBER).description("작성한 게시글 ID")
                                )
                        )
                );
    }

    @Test
    @DisplayName("게시글 삭제, 성공 시 삭제된 postId 응답")
    void deletePost() {
        doReturn(POST_ID_ELEMENT)
                .when(postService)
                .deletePost(any(), any());

        restDocs.cookie(ACCESS_TOKEN)
                .when().delete("/posts/{postId}", 1L)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .apply(document("post/delete-post",
                                requestCookieAccessTokenMandatory(),
                                pathParameters(
                                        parameterWithName("postId").description("삭제할 게시글의 고유 ID")
                                ),
                                getEnvelopPatternWithData().andWithPrefix("data.",
                                        fieldWithPath("postId").type(JsonFieldType.NUMBER).description("삭제한 게시글 ID")
                                )
                        )
                );
    }

    @Test
    @DisplayName("게시글 수정")
    void updatePost() {
        doReturn(POST_ID_ELEMENT)
                .when(postService)
                .updatePost(any(), any(), any());

        restDocs.cookie(ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(POST_PATCH_UPDATE_REQ_DTO1)
                .when().patch("/posts/{postId}", 1L)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .apply(document("post/update-post",
                                requestCookieAccessTokenMandatory(),
                                pathParameters(
                                        parameterWithName("postId").description("수정할 게시글의 고유 ID")
                                ),
                                requestFields(
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 내용"),
                                        fieldWithPath("anonymity").type(JsonFieldType.BOOLEAN).description("게시글 작성자의 익명 여부를 나타내는 필드"),
                                        fieldWithPath("images").type(JsonFieldType.ARRAY).description("게시글 사진 목록")
                                ).andWithPrefix("images[].",
                                        fieldWithPath("imagePath").type(JsonFieldType.STRING).description("s3에 저장된 경로를 의미하는 필드, s3에 저장된 데이터를 삭제할 때 사용").optional(),
                                        fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("s3에 저장된 이미지를 CDN URL 주소로 가져오기 위한 필드, 해당 URL에 접근만 해도 이미지에 접근할 수 있는 경로를 의미").optional()
                                ),
                                getEnvelopPatternWithData().andWithPrefix("data.",
                                        fieldWithPath("postId").type(JsonFieldType.NUMBER).description("수정한 게시글 ID")
                                )
                        )
                );
    }

    @Test
    @DisplayName("Hot 게시글 목록 조회(Cursor)")
    void findHotPostsByCursor() {
        doReturn(GET_POST_HOT_RES_DTO1)
                .when(postService)
                .findHotPostsByCursor(any());

        restDocs.cookie(ACCESS_TOKEN)
                .when().get("/posts/hot/cursor?cursor={cursor}&size={pageSize}", -1, 10)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .apply(document("post/find-hot-posts-by-cursor",
                                requestCookieAccessTokenNeedless(),
                                requestParameters(
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
                                )
                        )
                );
    }

    @Test
    @DisplayName("Hot 게시글 검색(Cursor), 제목 + 내용을 기준으로 검색")
    void searchHotPostsByCursor() {
        doReturn(GET_POST_HOT_RES_DTO2)
                .when(postService)
                .searchHotPostsByCursor(any());

        restDocs.cookie(ACCESS_TOKEN)
                .when().get("/posts/hot/search/cursor?&keyword={searchText}&cursor={cursor}&size={pageSize}", "취업", -1, 10)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .apply(document("post/search-hot-posts-by-cursor",
                                requestCookieAccessTokenNeedless(),
                                requestParameters(
                                        parameterWithName("keyword").description("검색하려는 게시글의 검색어, 최소 2글자"),
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
                                )
                        )
                );
    }

    @Test
    @DisplayName("내가 작성한 게시글 목록 조회(Cursor)")
    void findMyPostsByCursor() {
        doReturn(GET_POST_MY_RES_DTO1)
                .when(postService)
                .findMyPostsByCursor(any(), any());

        restDocs.cookie(ACCESS_TOKEN)
                .when().get("/posts/my/cursor?cursor={cursor}&size={pageSize}", -1, 10)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .apply(document("post/find-my-posts-by-cursor",
                                requestCookieAccessTokenMandatory(),
                                requestParameters(
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
                                )
                        )
                );
    }

    @Test
    @DisplayName("나의 스크랩 게시글 목록 조회(Cursor)")
    void findMyScrapPostsByCursor() {
        doReturn(GET_POST_MY_SCRAP_RES_DTO)
                .when(postService)
                .findMyScrapPostsByCursor(any(), any());

        restDocs.cookie(ACCESS_TOKEN)
                .when().get("/posts/my-scrap/cursor?cursor={cursor}&size={pageSize}", -1, 10)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .apply(document("post/find-my-scrap-posts-by-cursor",
                                requestCookieAccessTokenMandatory(),
                                requestParameters(
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
                                )
                        )
                );
    }
}