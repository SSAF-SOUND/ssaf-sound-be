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
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;

    public GetBoardListResDto findBoards() {
        return new GetBoardListResDto(boardRepository.findAll()
                .stream()
                .map(GetBoardResDto::fromEntity)
                .collect(Collectors.toList()));
    }
}
