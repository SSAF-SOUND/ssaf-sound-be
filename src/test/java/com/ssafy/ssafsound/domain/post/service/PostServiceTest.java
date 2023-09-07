package com.ssafy.ssafsound.domain.post.service;

import com.ssafy.ssafsound.domain.board.exception.BoardErrorInfo;
import com.ssafy.ssafsound.domain.board.exception.BoardException;
import com.ssafy.ssafsound.domain.board.repository.BoardRepository;
import com.ssafy.ssafsound.domain.member.repository.MemberRepository;
import com.ssafy.ssafsound.domain.post.domain.Post;
import com.ssafy.ssafsound.domain.post.dto.GetPostReqDto;
import com.ssafy.ssafsound.domain.post.dto.GetPostResDto;
import com.ssafy.ssafsound.domain.post.repository.*;
import com.ssafy.ssafsound.infra.storage.service.AwsS3StorageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.ssafy.ssafsound.global.util.fixture.PostFixture.POST_FIXTURE1;
import static com.ssafy.ssafsound.global.util.fixture.PostFixture.POST_FIXTURE2;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostLikeRepository postLikeRepository;

    @Mock
    private HotPostRepository hotPostRepository;

    @Mock
    private PostScrapRepository postScrapRepository;

    @Mock
    private PostImageRepository postImageRepository;

    @Mock
    private AwsS3StorageService awsS3StorageService;

    @Mock
    private PostConstantProvider postConstantProvider;

    @InjectMocks
    private PostService postService;


    @Test
    @DisplayName("정상적인 boardId, cursor, size가 주어졌다면 게시글 목록 조회가 성공합니다.")
    void Given_BoardIdAndCursorAndSize_When_findPosts_Then_Success() {
        // given
        GetPostReqDto getPostReqDto = GetPostReqDto.builder()
                .boardId(1L)
                .cursor(-1L)
                .size(10)
                .build();

        List<Post> posts = List.of(POST_FIXTURE1, POST_FIXTURE2);

        given(boardRepository.existsById(getPostReqDto.getBoardId())).willReturn(true);
        given(postRepository.findWithDetailsByBoardId(getPostReqDto.getBoardId(), getPostReqDto.getCursor(), getPostReqDto.getSize())).willReturn(posts);

        // when
        GetPostResDto response = postService.findPosts(getPostReqDto);

        // then
        assertThat(response).usingRecursiveComparison().isEqualTo(GetPostResDto.ofPosts(posts, getPostReqDto.getSize()));

        // verify
        verify(boardRepository, times(1)).existsById(getPostReqDto.getBoardId());
        verify(postRepository, times(1)).findWithDetailsByBoardId(getPostReqDto.getBoardId(), getPostReqDto.getCursor(), getPostReqDto.getSize());
    }

    @Test
    @DisplayName("존재하지 않은 boardId가 주어졌다면 게시글 목록 조회가 실패합니다.")
    void Given_BoardId_When_findPosts_Then_Fail() {
        // given
        GetPostReqDto getPostReqDto = GetPostReqDto.builder()
                .boardId(100L)
                .cursor(-1L)
                .size(10)
                .build();

        given(boardRepository.existsById(getPostReqDto.getBoardId())).willReturn(false);

        // when, then
        BoardException exception = assertThrows(BoardException.class, () -> postService.findPosts(getPostReqDto));
        assertEquals(BoardErrorInfo.NO_BOARD, exception.getInfo());

        // verify
        verify(boardRepository, times(1)).existsById(getPostReqDto.getBoardId());
    }

    @Test
    void findPost() {
    }

    @Test
    void likePost() {
    }

    @Test
    void deleteHotPostsUnderThreshold() {
    }

    @Test
    void scrapPost() {
    }

    @Test
    void writePost() {
    }

    @Test
    void deletePost() {
    }

    @Test
    void updatePost() {
    }

    @Test
    void findHotPosts() {
    }

    @Test
    void findMyPosts() {
    }

    @Test
    void findMyScrapPosts() {
    }

    @Test
    void searchPosts() {
    }

    @Test
    void searchHotPosts() {
    }
}