package com.ssafy.ssafsound.domain.board.dto;

import com.ssafy.ssafsound.domain.board.domain.Board;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetBoardElement {
    private Long boardId;
    private String title;
    private Boolean usedBoard;

    public static GetBoardElement from(Board board) {
        return GetBoardElement.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .usedBoard(board.getUsedBoard())
                .build();
    }
}
