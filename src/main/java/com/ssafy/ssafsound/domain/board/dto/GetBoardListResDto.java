package com.ssafy.ssafsound.domain.board.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class GetBoardListResDto {

    private List<GetBoardResDto> boards;

    public GetBoardListResDto(List<GetBoardResDto> boards) {
        this.boards = boards;
    }
}
