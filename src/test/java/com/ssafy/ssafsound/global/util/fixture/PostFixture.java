package com.ssafy.ssafsound.global.util.fixture;

import com.ssafy.ssafsound.domain.post.domain.Post;
import com.ssafy.ssafsound.domain.post.dto.GetPostDetailResDto;
import com.ssafy.ssafsound.domain.post.dto.GetPostResDto;

import java.util.List;

import static com.ssafy.ssafsound.global.util.fixture.BoardFixture.BOARD_FIXTURE1;
import static com.ssafy.ssafsound.global.util.fixture.MemberFixture.*;

public class PostFixture {

    public static final Integer PAGE_SIZE1 = 10;

    public static final Post POST_FIXTURE1 = Post.builder()
            .id(1L)
            .title("싸탈하고 싶다.")
            .content("싸탈하고 싶은 밤이네요")
            .anonymity(true)
            .board(BOARD_FIXTURE1)
            .member(MEMBER_WALTER)
            .build();

    public static final Post POST_FIXTURE2 = Post.builder()
            .id(2L)
            .title("삼성 B형을 봤는데")
            .content("결과가 암울해")
            .anonymity(false)
            .board(BOARD_FIXTURE1)
            .member(MEMBER_YONG)
            .build();

    public static final GetPostResDto GET_POST_RES_DTO1 = GetPostResDto.of(
            List.of(POST_FIXTURE1,
                    POST_FIXTURE2), PAGE_SIZE1);

    public static final GetPostDetailResDto GET_POST_DETAIL_RES_DTO1 = GetPostDetailResDto.of(
            POST_FIXTURE2, null);
}
