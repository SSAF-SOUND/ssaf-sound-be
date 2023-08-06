package com.ssafy.ssafsound.domain.board.dto;

import com.ssafy.ssafsound.domain.board.domain.Board;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class GetBoardResDto {
    private List<GetBoardElement> boards;

    public static GetBoardResDto from(List<Board> boards) {
        return GetBoardResDto.builder()
                .boards(boards.stream()
                        .map(GetBoardElement::from)
                        .collect(Collectors.toList()))
                .build();
    }

}
