package com.ssafy.ssafsound.domain.board.service;

import com.ssafy.ssafsound.domain.board.domain.Board;
import com.ssafy.ssafsound.domain.board.dto.GetBoardResDto;
import com.ssafy.ssafsound.domain.board.repository.BoardRepository;
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

    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private BoardService boardService;

    @Test
    @DisplayName("게시판 목록 조회가 성공적으로 수행됩니다.")
    void Given_Nothing_When_FindBoards_Then_Success() {
        List<Board> boards = List.of(BOARD_FIXTURE1, BOARD_FIXTURE2);

        // given
        given(boardRepository.findAll()).willReturn(boards);

        // when
        GetBoardResDto getBoardResDto = boardService.findBoards();

        // then
        assertThat(getBoardResDto.getBoards())
                .hasSize(2)
                .extracting("title")
                .containsExactly(BOARD_FIXTURE1.getTitle(), BOARD_FIXTURE2.getTitle());

        // verify
        verify(boardRepository).findAll();
    }
}