package com.ssafy.ssafsound.domain.post.service;

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
    @DisplayName("정상적인 BoardId, Cursor, Size가 주어졌다면 게시글 목록 조회가 성공합니다.")
    void Given_BoardIdAndCursorAndSize_When_findPosts_Then_Success() {
        GetPostReqDto getPostReqDto = GetPostReqDto.builder()
                .boardId(1L)
                .cursor(-1L)
                .size(10)
                .build();

        List<Post> posts = List.of(POST_FIXTURE1, POST_FIXTURE2);

        // given
        given(boardRepository.existsById(getPostReqDto.getBoardId())).willReturn(true);
        given(postRepository.findWithDetailsByBoardId(getPostReqDto.getBoardId(), getPostReqDto.getCursor(), getPostReqDto.getSize())).willReturn(posts);

        // when
        GetPostResDto getPostResDto = postService.findPosts(getPostReqDto);

        // then
        assertAll(
                () -> assertThat(getPostResDto.getPosts()).hasSize(2),
                () -> assertThat(getPostResDto.getPosts())
                        .extracting("title")
                        .containsExactly(POST_FIXTURE1.getTitle(), POST_FIXTURE2.getTitle()),
                () -> assertThat(getPostResDto.getPosts())
                        .extracting("boardTitle")
                        .containsExactly(POST_FIXTURE1.getBoard().getTitle(), POST_FIXTURE2.getBoard().getTitle())
//                () -> assertThat(response.getTitle()).isEqualTo(post.getTitle()),
//                () -> assertThat(response.getContent()).isEqualTo(post.getContent()),
//                () -> assertThat(response.isAuthorized()).isFalse(),
//                () -> assertThat(response.isModified()).isFalse(),
//                () -> assertThat(response.getCreatedAt()).isNotNull(),
//                () -> assertThat(response.getBoardId()).isEqualTo(postBoard.getBoard().getId())
        );

        // verify
    }

    @Test
    @DisplayName("존재하지 않은 BoardId가 주어졌다면 게시글 목록 조회가 실패합니다.")
    void Given_BoardId_When_findPosts_Then_Fail() {

    }

    @Test
    @DisplayName("최소값 보다 작은 Size가 주어졌다면 게시글 목록 조회가 실패합니다.")
    void Given_Size_When_findPosts_Then_Fail() {

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