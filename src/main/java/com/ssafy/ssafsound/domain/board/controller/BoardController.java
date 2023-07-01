package com.ssafy.ssafsound.domain.board.controller;

import com.ssafy.ssafsound.domain.board.dto.GetBoardResDto;
import com.ssafy.ssafsound.domain.board.service.BoardService;
import com.ssafy.ssafsound.global.common.response.EnvelopeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {
    private final BoardService boardService;

    @GetMapping
    public EnvelopeResponse<GetBoardResDto> findBoards() {
        return EnvelopeResponse.<GetBoardResDto>builder()
                .data(boardService.findBoards())
                .build();
    }
}
