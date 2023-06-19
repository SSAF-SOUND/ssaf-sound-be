package com.ssafy.ssafsound.domain.post.service;

import com.ssafy.ssafsound.domain.board.domain.Board;
import com.ssafy.ssafsound.domain.board.exception.BoardErrorInfo;
import com.ssafy.ssafsound.domain.board.exception.BoardException;
import com.ssafy.ssafsound.domain.board.repository.BoardRepository;
import com.ssafy.ssafsound.domain.post.dto.GetPostListResDto;
import com.ssafy.ssafsound.domain.post.dto.GetPostResDto;
import com.ssafy.ssafsound.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final BoardRepository boardRepository;

    @Transactional(readOnly = true)
    public GetPostListResDto findPosts(Long boardId, Pageable pageable) {
        boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardException(BoardErrorInfo.NO_BOARD_ID));

        return new GetPostListResDto(postRepository.findAllByBoardId(boardId, pageable)
                .stream()
                .map(GetPostResDto::from)
                .collect(Collectors.toList()));
    }
}
