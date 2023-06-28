package com.ssafy.ssafsound.domain.board.service;

import com.ssafy.ssafsound.domain.board.dto.GetBoardResDto;
import com.ssafy.ssafsound.domain.board.dto.GetBoard;
import com.ssafy.ssafsound.domain.board.exception.BoardErrorInfo;
import com.ssafy.ssafsound.domain.board.exception.BoardException;
import com.ssafy.ssafsound.domain.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    @Transactional(readOnly = true)
    public GetBoardResDto findBoards() {
        List<GetBoard> boards = boardRepository.findAll()
                .stream()
                .map(board -> GetBoard.from(board))
                .collect(Collectors.toList());

        if (boards.size() == 0) {
            throw new BoardException(BoardErrorInfo.EMPTY_BOARD);
        }

        return GetBoardResDto.builder()
                .boards(boards)
                .build();
    }
}
