package com.ssafy.ssafsound.global.util.fixture;

import com.ssafy.ssafsound.domain.post.domain.Post;
import com.ssafy.ssafsound.domain.post.dto.*;

import java.util.List;

import static com.ssafy.ssafsound.global.util.fixture.BoardFixture.BOARD_FIXTURE1;
import static com.ssafy.ssafsound.global.util.fixture.BoardFixture.BOARD_FIXTURE2;
import static com.ssafy.ssafsound.global.util.fixture.MemberFixture.*;

public class PostFixture {

    public static final Post POST_FIXTURE1 = Post.builder()
            .id(1L)
            .title("싸탈하고 싶다.")
            .content("싸탈하고 싶은 밤이네요")
            .anonymity(false)
            .board(BOARD_FIXTURE1)
            .member(MEMBER_KIM)
            .build();

    public static final Post POST_FIXTURE2 = Post.builder()
            .id(2L)
            .title("삼성 B형을 봤는데")
            .content("결과가 암울해")
            .anonymity(true)
            .board(BOARD_FIXTURE1)
            .member(MEMBER_YONG)
            .build();

    public static final Post POST_FIXTURE3 = Post.builder()
            .id(3L)
            .title("안녕하세요 취업 게시판의 첫 글이네요")
            .content("취업이 뭐죠??")
            .anonymity(true)
            .board(BOARD_FIXTURE2)
            .member(MEMBER_YONG)
            .build();

    public static final GetPostResDto GET_POST_RES_DTO1 = GetPostResDto.of(
            List.of(POST_FIXTURE1,
                    POST_FIXTURE2), 10);

    public static final GetPostResDto GET_POST_RES_DTO2 = GetPostResDto.of(
            List.of(POST_FIXTURE3), 10);


    public static final GetPostDetailResDto GET_POST_DETAIL_RES_DTO1 = GetPostDetailResDto.of(
            POST_FIXTURE1, null);

    public static final GetPostDetailResDto GET_POST_DETAIL_RES_DTO2 = GetPostDetailResDto.of(
            POST_FIXTURE2, MEMBER_YONG);

    public static final PostPostWriteReqDto POST_POST_WRITE_REQ_DTO1 = PostPostWriteReqDto.builder()
            .title("안녕하세요 첫 글이네요")
            .content("싸피도 드디어 익명 커뮤니티가 생기다니..")
            .anonymity(true)
            .images(List.of())
            .build();

    public static final PostPatchUpdateReqDto POST_PATCH_UPDATE_REQ_DTO1 = PostPatchUpdateReqDto.builder()
            .title("수정한 게시글의 제목")
            .content("수정한 게시글의 내용입니다.")
            .anonymity(false)
            .images(List.of())
            .build();

    public static final PostIdElement POST_ID_ELEMENT = PostIdElement.builder()
            .postId(1L)
            .build();

    public static final PostPostLikeResDto POST_POST_LIKE_RES_DTO = PostPostLikeResDto.builder()
            .likeCount(10)
            .liked(true)
            .build();

    public static final PostPostScrapResDto POST_POST_SCRAP_RES_DTO = PostPostScrapResDto.builder()
            .scrapCount(9)
            .scraped(false)
            .build();
}
