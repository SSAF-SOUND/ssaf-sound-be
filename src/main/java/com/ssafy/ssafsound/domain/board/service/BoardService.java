package com.ssafy.ssafsound.domain.board.service;

import com.ssafy.ssafsound.domain.board.dto.GetBoardListResDto;
import com.ssafy.ssafsound.domain.board.dto.GetBoardResDto;
import com.ssafy.ssafsound.domain.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    @Transactional(readOnly = true)
    public GetBoardListResDto findBoards() {
        return GetBoardListResDto.builder()
                .boards(boardRepository.findAll()
                        .stream()
                        .map(GetBoardResDto::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
