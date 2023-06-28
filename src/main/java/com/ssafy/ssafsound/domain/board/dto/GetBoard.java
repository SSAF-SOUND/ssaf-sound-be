package com.ssafy.ssafsound.domain.board.dto;

import com.ssafy.ssafsound.domain.board.domain.Board;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetBoard {
    private Long boardId;
    private String title;
    private Boolean usedBoard;

    public static GetBoard from(Board board) {
        System.out.println(board.getTitle());
        return GetBoard.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .usedBoard(board.getUsedBoard())
                .build();
    }
}
