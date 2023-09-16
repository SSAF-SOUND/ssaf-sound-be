package com.ssafy.ssafsound.domain.post.service;

import static com.ssafy.ssafsound.global.util.fixture.PostFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ssafy.ssafsound.domain.board.exception.BoardErrorInfo;
import com.ssafy.ssafsound.domain.board.exception.BoardException;
import com.ssafy.ssafsound.domain.board.repository.BoardRepository;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.exception.MemberErrorInfo;
import com.ssafy.ssafsound.domain.member.exception.MemberException;
import com.ssafy.ssafsound.domain.member.repository.MemberRepository;
import com.ssafy.ssafsound.domain.post.domain.Post;
import com.ssafy.ssafsound.domain.post.domain.PostLike;
import com.ssafy.ssafsound.domain.post.dto.GetPostDetailResDto;
import com.ssafy.ssafsound.domain.post.dto.GetPostReqDto;
import com.ssafy.ssafsound.domain.post.dto.GetPostResDto;
import com.ssafy.ssafsound.domain.post.dto.PostCommonLikeResDto;
import com.ssafy.ssafsound.domain.post.exception.PostErrorInfo;
import com.ssafy.ssafsound.domain.post.exception.PostException;
import com.ssafy.ssafsound.domain.post.repository.HotPostRepository;
import com.ssafy.ssafsound.domain.post.repository.PostImageRepository;
import com.ssafy.ssafsound.domain.post.repository.PostLikeRepository;
import com.ssafy.ssafsound.domain.post.repository.PostRepository;
import com.ssafy.ssafsound.domain.post.repository.PostScrapRepository;
import com.ssafy.ssafsound.global.util.fixture.MemberFixture;
import com.ssafy.ssafsound.infra.storage.service.AwsS3StorageService;

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
	@DisplayName("유효한 boardId, cursor, size가 주어졌다면 게시글 목록 조회가 성공합니다.")
	void Given_BoardIdAndCursorAndSize_When_findPosts_Then_Success() {
		// given
		GetPostReqDto getPostReqDto = GetPostReqDto.builder()
			.boardId(1L)
			.cursor(-1L)
			.size(10)
			.build();

		List<Post> posts = List.of(POST_FIXTURE1, POST_FIXTURE2);

		given(boardRepository.existsById(getPostReqDto.getBoardId())).willReturn(true);
		given(postRepository.findWithDetailsByBoardId(getPostReqDto.getBoardId(), getPostReqDto.getCursor(),
			getPostReqDto.getSize())).willReturn(posts);

		// when
		GetPostResDto response = postService.findPosts(getPostReqDto);

		// then
		assertThat(response).usingRecursiveComparison()
			.isEqualTo(GetPostResDto.ofPosts(posts, getPostReqDto.getSize()));

		// verify
		verify(boardRepository, times(1)).existsById(getPostReqDto.getBoardId());
		verify(postRepository, times(1)).findWithDetailsByBoardId(getPostReqDto.getBoardId(), getPostReqDto.getCursor(),
			getPostReqDto.getSize());
	}

	@Test
	@DisplayName("유효하지 않은 boardId가 주어졌다면 게시글 목록 조회에 예외를 발생합니다.")
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
	@DisplayName("로그인 시 유효한 postId와 loginMemberId가 주어졌다면 게시글 상세 보기가 성공합니다.")
	void Given_PostIdAndLoginMemberId_When_findPost_Then_Success() {
		// given
		Post post = POST_FIXTURE1;
		Member member = MemberFixture.GENERAL_MEMBER;

		given(postRepository.findWithMemberAndPostImageFetchById(post.getId())).willReturn(Optional.of(post));
		given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));

		// when
		GetPostDetailResDto response = postService.findPost(post.getId(), member.getId());

		// then
		assertThat(response).usingRecursiveComparison()
			.isEqualTo(GetPostDetailResDto.of(post, member));

		// verify
		verify(postRepository, times(1)).findWithMemberAndPostImageFetchById(post.getId());
		verify(memberRepository, times(1)).findById(member.getId());
	}

	@Test
	@DisplayName("비 로그인 시 유효한 postId가 주어졌다면 게시글 상세 보기가 성공합니다.")
	void Given_PostId_When_findPost_Then_Success() {
		// given
		Post post = POST_FIXTURE1;

		given(postRepository.findWithMemberAndPostImageFetchById(post.getId())).willReturn(Optional.of(post));

		// when
		GetPostDetailResDto response = postService.findPost(post.getId(), null);

		// then
		assertThat(response).usingRecursiveComparison()
			.isEqualTo(GetPostDetailResDto.of(post, null));

		// verify
		verify(postRepository, times(1)).findWithMemberAndPostImageFetchById(post.getId());

	}

	@Test
	@DisplayName("유효하지 않은 postId가 주어졌다면 게시글 상세보기에 예외를 발생합니다.")
	void Given_InvalidPostId_When_findPost_Then_Success() {
		// given
		Long postId = 100L;

		given(postRepository.findWithMemberAndPostImageFetchById(postId)).willReturn(Optional.empty());

		// when, then
		PostException exception = assertThrows(PostException.class, () -> postService.findPost(postId, null));
		assertEquals(PostErrorInfo.NOT_FOUND_POST, exception.getInfo());

		// verify
		verify(postRepository, times(1)).findWithMemberAndPostImageFetchById(postId);
	}

	@Test
	@DisplayName("로그인 시 유효하지 않은 loginMemberId가 주어졌다면 게시글 상세보기에 예외를 발생합니다.")
	void Given_InvalidLoginMemberId_When_findPost_Then_Success() {
		// given
		Post post = POST_FIXTURE1;
		Long memberId = 100L;

		given(postRepository.findWithMemberAndPostImageFetchById(post.getId())).willReturn(Optional.of(post));
		given(memberRepository.findById(memberId)).willReturn(Optional.empty());

		// when, then
		MemberException exception = assertThrows(MemberException.class,
			() -> postService.findPost(post.getId(), memberId));
		assertEquals(MemberErrorInfo.MEMBER_NOT_FOUND_BY_ID, exception.getInfo());

		// verify
		verify(postRepository, times(1)).findWithMemberAndPostImageFetchById(post.getId());
		verify(memberRepository, times(1)).findById(memberId);
	}

	@Test
	@DisplayName("게시글을 좋아요 하지 않았다면 좋아요가 저장됩니다.")
	void Given_PostIdAndLoginMemberId_When_SaveLikePost_Then_Success() {
		// given
		Post post = POST_FIXTURE1;
		Member member = MemberFixture.GENERAL_MEMBER;
		int likeCount = 4;

		given(postRepository.findById(post.getId())).willReturn(Optional.of(post));
		given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
		given(postLikeRepository.findByPostIdAndMemberId(post.getId(), member.getId())).willReturn(Optional.empty());
		given(postLikeRepository.countByPostId(post.getId())).willReturn(likeCount);
		given(postConstantProvider.getHOT_POST_LIKES_THRESHOLD()).willReturn(10L);

		// when
		PostCommonLikeResDto response = postService.likePost(post.getId(), member.getId());

		// then
		assertThat(response).usingRecursiveComparison()
			.isEqualTo(new PostCommonLikeResDto(likeCount + 1, true));

		// verify
		verify(postRepository, times(1)).findById(post.getId());
		verify(memberRepository, times(1)).findById(member.getId());
		verify(postLikeRepository, times(1)).findByPostIdAndMemberId(post.getId(), member.getId());
		verify(postLikeRepository, times(1)).countByPostId(post.getId());
		verify(postConstantProvider, times(1)).getHOT_POST_LIKES_THRESHOLD();
		verify(postLikeRepository, times(1)).save(any());

		verify(hotPostRepository, times(0)).save(any());
	}

	@Test
	@DisplayName("게시글을 이미 좋아요 했다면 좋아요가 취소됩니다.")
	void Given_PostIdAndLoginMemberId_When_DeleteLikePost_Then_Success() {
		// given
		Post post = POST_FIXTURE1;
		Member member = MemberFixture.GENERAL_MEMBER;
		PostLike postLike = PostLike.of(post, member);
		int likeCount = 4;

		given(postRepository.findById(post.getId())).willReturn(Optional.of(post));
		given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
		given(postLikeRepository.findByPostIdAndMemberId(post.getId(), member.getId())).willReturn(
			Optional.of(postLike));
		given(postLikeRepository.countByPostId(post.getId())).willReturn(likeCount);

		// when
		PostCommonLikeResDto response = postService.likePost(post.getId(), member.getId());

		// then
		assertThat(response).usingRecursiveComparison()
			.isEqualTo(new PostCommonLikeResDto(likeCount - 1, false));

		// verify
		verify(postRepository, times(1)).findById(post.getId());
		verify(memberRepository, times(1)).findById(member.getId());
		verify(postLikeRepository, times(1)).findByPostIdAndMemberId(post.getId(), member.getId());
		verify(postLikeRepository, times(1)).countByPostId(post.getId());
		verify(postLikeRepository, times(1)).delete(any());
	}

	@Test
	@DisplayName("좋아요가 특정 개수를 달성했다면 Hot 게시글로 등록됩니다.")
	void Given_PostIdAndLoginMemberId_When_NotExistsHotPost_Then_Success() {
		// given
		Post post = POST_FIXTURE1;
		Member member = MemberFixture.GENERAL_MEMBER;
		int likeCount = 9;

		given(postRepository.findById(post.getId())).willReturn(Optional.of(post));
		given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
		given(postLikeRepository.findByPostIdAndMemberId(post.getId(), member.getId())).willReturn(Optional.empty());
		given(postLikeRepository.countByPostId(post.getId())).willReturn(likeCount);
		given(postConstantProvider.getHOT_POST_LIKES_THRESHOLD()).willReturn(10L);

		// when
		PostCommonLikeResDto response = postService.likePost(post.getId(), member.getId());

		// then
		assertThat(response).usingRecursiveComparison()
			.isEqualTo(new PostCommonLikeResDto(likeCount + 1, true));

		// verify
		verify(postRepository, times(1)).findById(post.getId());
		verify(memberRepository, times(1)).findById(member.getId());
		verify(postLikeRepository, times(1)).findByPostIdAndMemberId(post.getId(), member.getId());
		verify(postLikeRepository, times(1)).countByPostId(post.getId());
		verify(postConstantProvider, times(1)).getHOT_POST_LIKES_THRESHOLD();
		verify(hotPostRepository, times(1)).save(any());
		verify(hotPostRepository, times(1)).existsByPostId(post.getId());
	}

	@Test
	@DisplayName("좋아요가 특정 개수를 달성했지만 이미 Hot 게시글이라면 등록되지 않습니다.")
	void Given_PostIdAndLoginMemberId_When_ExistsHotPost_Then_Success() {
		// given

		// when

		// then

		// verify
	}

	@Test
	@DisplayName("유효하지 않은 loginMemberId가 주어졌다면 게시글 좋아요에 예외를 발생합니다.")
	void Given_InvalidLoginMemberId_When_likePost_Then_Success() {
		// given

		// when

		// then

		// verify
	}

	@Test
	@DisplayName("유효하지 않은 postId가 주어졌다면 게시글 좋아요에 예외를 발생합니다.")
	void Given_InvalidPostId_When_likePost_Then_Success() {
		// given

		// when

		// then

		// verify
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