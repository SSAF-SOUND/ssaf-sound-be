package com.ssafy.ssafsound.domain.board.service;

import com.ssafy.ssafsound.domain.board.domain.Board;
import com.ssafy.ssafsound.domain.board.dto.GetBoardResDto;
import com.ssafy.ssafsound.domain.board.repository.BoardRepository;
import com.ssafy.ssafsound.domain.post.dto.GetPostResDto;
import com.ssafy.ssafsound.global.util.fixture.BoardFixture;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.ssafy.ssafsound.global.util.fixture.BoardFixture.*;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    private final BoardFixture boardFixture = new BoardFixture();

    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private BoardService boardService;

    @Test
    @DisplayName("게시판 목록 조회가 성공적으로 수행됩니다.")
    void Given_Empty_When_FindBoards_Then_Success() {
        Board freeBoard = boardFixture.getFreeBoard();
        Board jobBoard = boardFixture.getJobBoard();

        List<Board> boards = List.of(freeBoard, jobBoard);

        // given
        given(boardRepository.findAllByUsedBoardTrue()).willReturn(boards);

        // when
        GetBoardResDto response = boardService.findBoards();

        // then
        assertThat(response).usingRecursiveComparison()
            .isEqualTo(GetBoardResDto.from(boards));

        // verify
        verify(boardRepository).findAllByUsedBoardTrue();
    }
}