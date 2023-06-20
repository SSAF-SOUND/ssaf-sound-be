package com.ssafy.ssafsound.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetBoardListResDto {
    private List<GetBoardResDto> boards;

}
