package com.ssafy.ssafsound.domain.board.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GetBoardResDto {
    private List<GetBoard> boards;

}
