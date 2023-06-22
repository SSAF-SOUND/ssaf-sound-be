package com.ssafy.ssafsound.domain.post.service;

import com.ssafy.ssafsound.domain.board.exception.BoardErrorInfo;
import com.ssafy.ssafsound.domain.board.exception.BoardException;
import com.ssafy.ssafsound.domain.board.repository.BoardRepository;
import com.ssafy.ssafsound.domain.member.repository.MemberRepository;
import com.ssafy.ssafsound.domain.post.domain.PostScrap;
import com.ssafy.ssafsound.domain.post.dto.GetPostListResDto;
import com.ssafy.ssafsound.domain.post.dto.GetPostResDto;
import com.ssafy.ssafsound.domain.post.repository.PostRepository;
import com.ssafy.ssafsound.domain.post.repository.PostScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final BoardRepository boardRepository;
    private final PostRepository postRepository;
    private final PostScrapRepository postScrapRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public GetPostListResDto findPosts(Long boardId, Pageable pageable) {
        boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardException(BoardErrorInfo.NO_BOARD_ID));

        return new GetPostListResDto(postRepository.findAllByBoardId(boardId, pageable)
                .stream()
                .map(GetPostResDto::from)
                .collect(Collectors.toList()));
    }

    @Transactional
    public void postScrap(Long postId, Long memberId) {
        PostScrap postScrap = postScrapRepository.findByPostIdAndMemberId(postId, memberId)
                .orElse(null);
        togglePostScrap(postId, memberId, postScrap);
    }

    private void togglePostScrap(Long postId, Long memberId, PostScrap postScrap) {
        if (postScrap != null) {
            deleteScrapIfAlreadyExists(postScrap);
            return;
        }
        saveScrap(postId, memberId);
    }

    private void saveScrap(Long postId, Long memberId) {
        PostScrap postScrap = PostScrap.builder()
                .post(postRepository.getReferenceById(postId))
                .member(memberRepository.getReferenceById(memberId))
                .build();
        postScrapRepository.save(postScrap);
    }

    private void deleteScrapIfAlreadyExists(PostScrap postScrap) {
        postScrapRepository.delete(postScrap);
    }

}
