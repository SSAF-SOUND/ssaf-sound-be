package com.ssafy.ssafsound.domain.board.dto;

import com.ssafy.ssafsound.domain.board.domain.Board;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetBoardElement {
    private Long boardId;
    private String title;
    private String imageUrl;
    private String description;

    public static GetBoardElement from(Board board) {
        return GetBoardElement.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .imageUrl(board.getImageUrl())
                .description(board.getDescription())
                .build();
    }
}
