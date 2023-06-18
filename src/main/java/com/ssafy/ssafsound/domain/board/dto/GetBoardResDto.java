package com.ssafy.ssafsound.domain.board.dto;

import com.ssafy.ssafsound.domain.board.domain.Board;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetBoardResDto {
    private Long boardId;
    private String title;
    private boolean usedBoard;

    public static GetBoardResDto fromEntity(Board board) {
        return GetBoardResDto.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .usedBoard(board.getUsedBoard())
                .build();
    }
}
