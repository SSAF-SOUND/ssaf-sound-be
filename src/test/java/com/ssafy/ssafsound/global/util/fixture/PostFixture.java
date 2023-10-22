package com.ssafy.ssafsound.global.util.fixture;

import com.ssafy.ssafsound.domain.post.domain.Post;
import com.ssafy.ssafsound.domain.post.dto.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PostFixture {

    private static final MemberFixture memberFixture = new MemberFixture();
    private static final BoardFixture boardFixture = new BoardFixture();

    public static final Post POST_FIXTURE1 = Post.builder()
            .id(1L)
            .title("싸탈하고 싶다.")
            .content("싸탈하고 싶은 밤이네요")
            .anonymity(false)
            .board(boardFixture.getFreeBoard())
            .member(memberFixture.createMember())
            .build();

    public static final Post POST_FIXTURE2 = Post.builder()
            .id(2L)
            .title("삼성 B형을 봤는데")
            .content("결과가 암울해")
            .anonymity(true)
            .board(boardFixture.getFreeBoard())
            .member(memberFixture.createMember())
            .build();

    public static final Post POST_FIXTURE3 = Post.builder()
            .id(3L)
            .title("안녕하세요 취업 게시판의 첫 글이네요")
            .content("취업이 뭐죠??")
            .anonymity(true)
            .board(boardFixture.getJobBoard())
            .member(memberFixture.createMember())
            .build();

    public static final GetPostElement GET_POST_ELEMENT1 = GetPostElement.builder()
            .boardId(1L)
            .boardTitle("자유 게시판")
            .postId(4L)
            .title("싸피 어떤가요??")
            .content("저도 싸피에서 교육들으면 개발실력 엄청 오르겠죠?")
            .likeCount(11)
            .commentCount(29)
            .createdAt(LocalDateTime.now())
            .nickname("이용준")
            .anonymity(false)
            .thumbnail("썸네일 URL")
            .build();

    public static final GetPostElement GET_POST_ELEMENT2 = GetPostElement.builder()
            .boardId(1L)
            .boardTitle("자유 게시판")
            .postId(5L)
            .title("삼성 B형 시험을 준비하려면 뭘 해야할까요?")
            .content("역시 B형 특강을 열심히 듣는게 맞나요?")
            .likeCount(5)
            .commentCount(14)
            .createdAt(LocalDateTime.now())
            .nickname("익명")
            .anonymity(true)
            .thumbnail("썸네일 URL")
            .build();

    public static final GetPostElement GET_POST_ELEMENT3 = GetPostElement.builder()
            .boardId(1L)
            .boardTitle("자유 게시판")
            .postId(6L)
            .title("안녕하세요 반갑습니다.")
            .content("SSAFY 9기 합격했습니다!!")
            .likeCount(3)
            .commentCount(2)
            .createdAt(LocalDateTime.now())
            .nickname("이용준")
            .anonymity(false)
            .thumbnail("썸네일 URL")
            .build();

    public static final GetPostElement GET_POST_HOT_ELEMENT1 = GetPostElement.builder()
            .boardId(2L)
            .boardTitle("취업 게시판")
            .postId(7L)
            .title("취업을 하기 위한 꿀팁")
            .content("열심히 SSAFY 9기를 수료하시면 취업에 성공하실겁니다.")
            .likeCount(102)
            .commentCount(33)
            .createdAt(LocalDateTime.now())
            .nickname("이용준")
            .anonymity(false)
            .thumbnail("썸네일 URL")
            .build();

    public static final GetPostElement GET_POST_HOT_ELEMENT2 = GetPostElement.builder()
            .boardId(1L)
            .boardTitle("자유 게시판")
            .postId(7L)
            .title("Hot 게시글에 가는 방법")
            .content("은 실력입니다.")
            .likeCount(202)
            .commentCount(54)
            .createdAt(LocalDateTime.now())
            .nickname("익명")
            .anonymity(true)
            .thumbnail("썸네일 URL")
            .build();

    public static final GetPostElement GET_POST_MY_ELEMENT1 = GetPostElement.builder()
            .boardId(1L)
            .boardTitle("자유 게시판")
            .postId(6L)
            .title("안녕하세요 반갑습니다.")
            .content("SSAFY 9기 합격했습니다!!")
            .likeCount(3)
            .commentCount(2)
            .createdAt(LocalDateTime.now())
            .nickname("이용준")
            .anonymity(false)
            .thumbnail("썸네일 URL")
            .build();

    public static final GetPostElement GET_POST_MY_ELEMENT2 = GetPostElement.builder()
            .boardId(2L)
            .boardTitle("취업 게시판")
            .postId(7L)
            .title("취업을 하기 위한 꿀팁")
            .content("열심히 SSAFY 9기를 수료하시면 취업에 성공하실겁니다.")
            .likeCount(102)
            .commentCount(33)
            .createdAt(LocalDateTime.now())
            .nickname("이용준")
            .anonymity(false)
            .thumbnail("썸네일 URL")
            .build();

    public static final GetPostCursorResDto GET_POST_CURSOR_RES_DTO1 = GetPostCursorResDto.ofPosts(
            List.of(POST_FIXTURE1,
                    POST_FIXTURE2), 10);

    public static final GetPostCursorResDto GET_POST_CURSOR_RES_DTO2 = GetPostCursorResDto.ofPosts(
            List.of(POST_FIXTURE3), 10);

    public static final GetPostCursorResDto GET_POST_CURSOR_RES_DTO3 = GetPostCursorResDto.builder()
            .posts(List.of(GET_POST_ELEMENT1,
                    GET_POST_ELEMENT2))
            .cursor(null)
            .build();

    public static final GetPostCursorResDto GET_POST_CURSOR_RES_DTO4 = GetPostCursorResDto.builder()
            .posts(List.of(GET_POST_ELEMENT3))
            .cursor(null)
            .build();

    public static final GetPostOffsetResDto GET_POST_OFFSET_RES_DTO1 = GetPostOffsetResDto.ofPosts(
            List.of(POST_FIXTURE1, POST_FIXTURE2));

    public static final GetPostOffsetResDto GET_POST_OFFSET_RES_DTO2 = GetPostOffsetResDto.ofPosts(
            List.of(POST_FIXTURE3));

    public static final GetPostOffsetResDto GET_POST_OFFSET_RES_DTO3 = GetPostOffsetResDto.builder()
            .posts(List.of(GET_POST_ELEMENT1,
                    GET_POST_ELEMENT2))
            .build();

    public static final GetPostOffsetResDto GET_POST_OFFSET_RES_DTO4 = GetPostOffsetResDto.builder()
            .posts(List.of(GET_POST_ELEMENT3))
            .build();

    public static final GetPostCursorResDto GET_POST_CURSOR_HOT_RES_DTO1 = GetPostCursorResDto.builder()
            .posts(List.of(GET_POST_HOT_ELEMENT1,
                    GET_POST_HOT_ELEMENT2))
            .cursor(null)
            .build();

    public static final GetPostCursorResDto GET_POST_CURSOR_HOT_RES_DTO2 = GetPostCursorResDto.builder()
            .posts(List.of(GET_POST_HOT_ELEMENT1))
            .cursor(null)
            .build();

    public static final GetPostOffsetResDto GET_POST_OFFSET_HOT_RES_DTO1 = GetPostOffsetResDto.builder()
            .posts(List.of(GET_POST_HOT_ELEMENT1,
                    GET_POST_HOT_ELEMENT2))
            .build();

    public static final GetPostOffsetResDto GET_POST_OFFSET_HOT_RES_DTO2 = GetPostOffsetResDto.builder()
            .posts(List.of(GET_POST_HOT_ELEMENT1))
            .build();

    public static final GetPostCursorResDto GET_POST_MY_RES_DTO1 = GetPostCursorResDto.builder()
            .posts(List.of(GET_POST_MY_ELEMENT1,
                    GET_POST_MY_ELEMENT2))
            .cursor(null)
            .build();

    public static final GetPostCursorResDto GET_POST_MY_SCRAP_RES_DTO = GetPostCursorResDto.builder()
            .posts(List.of(GET_POST_ELEMENT2,
                    GET_POST_ELEMENT1))
            .cursor(null)
            .build();

    public static final GetPostDetailResDto GET_POST_DETAIL_RES_DTO1 = GetPostDetailResDto.of(
            POST_FIXTURE1, null);

    public static final GetPostDetailResDto GET_POST_DETAIL_RES_DTO2 = GetPostDetailResDto.of(
            POST_FIXTURE2, memberFixture.createMember());

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

    public static final PostCommonLikeResDto POST_POST_LIKE_RES_DTO = PostCommonLikeResDto.builder()
            .likeCount(10)
            .liked(true)
            .build();

    public static final PostPostScrapResDto POST_POST_SCRAP_RES_DTO = PostPostScrapResDto.builder()
            .scrapCount(9)
            .scraped(false)
            .build();

    public static List<ImageInfo> createImageInfos(int size) {
        List<ImageInfo> imageInfos = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            imageInfos.add(ImageInfo.builder()
                    .imagePath("imagePath" + i)
                    .imageUrl("imageUrl" + i)
                    .build());
        }
        return imageInfos;
    }
}
