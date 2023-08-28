package com.ssafy.ssafsound.global.util.fixture;

import com.ssafy.ssafsound.domain.board.domain.Board;
import com.ssafy.ssafsound.domain.board.dto.GetBoardResDto;

import java.util.ArrayList;
import java.util.List;

public class BoardFixture {

    public static final Board BOARD_FIXTURE1 = Board.builder()
            .id(1L)
            .title("자유 게시판")
            .imageUrl("IMAGE URL")
            .description("자유롭게 떠드는 공간입니다.")
            .build();

    public static final Board BOARD_FIXTURE2 = Board.builder()
            .id(2L)
            .title("취업 게시판")
            .imageUrl("IMAGE URL")
            .description("취업에 대한 정보를 공유해보세요.")
            .build();

    public static final GetBoardResDto GET_BOARD_RES_DTO1 = GetBoardResDto.from(
            List.of(BOARD_FIXTURE1,
                    BOARD_FIXTURE2)
    );
}
