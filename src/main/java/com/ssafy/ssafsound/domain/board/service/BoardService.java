package com.ssafy.ssafsound.domain.board.service;

import com.ssafy.ssafsound.domain.board.domain.Board;
import com.ssafy.ssafsound.domain.board.dto.GetBoardResDto;
import com.ssafy.ssafsound.domain.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    @Transactional(readOnly = true)
    public GetBoardResDto findBoards() {
        List<Board> boards = boardRepository.findAll();
        return GetBoardResDto.from(boards);
    }
}
