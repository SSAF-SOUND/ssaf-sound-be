package com.ssafy.ssafsound.domain.post.service;

import com.ssafy.ssafsound.domain.board.domain.Board;
import com.ssafy.ssafsound.domain.board.exception.BoardErrorInfo;
import com.ssafy.ssafsound.domain.board.exception.BoardException;
import com.ssafy.ssafsound.domain.board.repository.BoardRepository;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.exception.MemberErrorInfo;
import com.ssafy.ssafsound.domain.member.exception.MemberException;
import com.ssafy.ssafsound.domain.member.repository.MemberRepository;
import com.ssafy.ssafsound.domain.post.domain.*;
import com.ssafy.ssafsound.domain.post.dto.GetPostDetailResDto;
import com.ssafy.ssafsound.domain.post.dto.GetPostResDto;
import com.ssafy.ssafsound.domain.post.exception.PostErrorInfo;
import com.ssafy.ssafsound.domain.post.exception.PostException;
import com.ssafy.ssafsound.domain.post.repository.*;
import com.ssafy.ssafsound.domain.post.dto.*;
import com.ssafy.ssafsound.infra.storage.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {
    @Value("${spring.constant.post.HOT_POST_LIKES_THRESHOLD}")
    private Long HOT_POST_LIKES_THRESHOLD;

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final HotPostRepository hotPostRepository;
    private final PostScrapRepository postScrapRepository;
    private final PostReportRepository postReportRepository;
    private final PostImageRepository postImageRepository;

    @Transactional(readOnly = true)
    public GetPostResDto findPosts(Long boardId, Pageable pageable) {
        if (!boardRepository.existsById(boardId)) {
            throw new BoardException(BoardErrorInfo.NO_BOARD);
        }

        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        List<Post> posts = postRepository.findWithDetailsByBoardId(boardId, pageRequest);

        return GetPostResDto.from(posts);
    }

    @Transactional(readOnly = true)
    public GetPostDetailResDto findPost(Long postId, Long loginMemberId) {
        Post post = postRepository.findWithMemberAndPostImageFetchById(postId)
                .orElseThrow(() -> new PostException(PostErrorInfo.NOT_FOUND_POST));

        if (loginMemberId != null) {
            Member member = memberRepository.findById(loginMemberId)
                    .orElseThrow(() -> new MemberException(MemberErrorInfo.MEMBER_NOT_FOUND_BY_ID));
            return GetPostDetailResDto.of(post, member);
        }

        return GetPostDetailResDto.of(post, null);
    }

    @Transactional
    public void likePost(Long postId, Long loginMemberId) {
        PostLike postLike = postLikeRepository.findByPostIdAndMemberId(postId, loginMemberId)
                .orElse(null);
        togglePostLike(postId, loginMemberId, postLike);
    }

    private void togglePostLike(Long postId, Long loginMemberId, PostLike postLike) {
        if (postLike != null) {
            deleteLike(postLike);
            return;
        }
        saveLike(postId, loginMemberId);
        if (isSelectedHotPost(postId)) {
            saveHotPost(postId);
        }
    }

    private void saveLike(Long postId, Long loginMemberId) {
        PostLike postLike = PostLike.builder()
                .post(postRepository.getReferenceById(postId))
                .member(memberRepository.getReferenceById(loginMemberId))
                .build();
        postLikeRepository.save(postLike);
    }

    private void deleteLike(PostLike postLike) {
        postLikeRepository.delete(postLike);
    }


    private boolean isSelectedHotPost(Long postId) {
        return postLikeRepository.countByPostId(postId) >= HOT_POST_LIKES_THRESHOLD;
    }

    private void saveHotPost(Long postId) {
        HotPost hotPost = HotPost.builder()
                .post(postRepository.findById(postId).
                        orElseThrow(() -> new PostException(PostErrorInfo.NOT_FOUND_POST)))
                .build();
        hotPostRepository.save(hotPost);
    }

    @Transactional
    public void deleteHotPostsUnderThreshold(Long threshold) {
        hotPostRepository.deleteHotPostsUnderThreshold(threshold);
    }

    @Transactional
    public void scrapPost(Long postId, Long loginMemberId) {
        PostScrap postScrap = postScrapRepository.findByPostIdAndMemberId(postId, loginMemberId)
                .orElse(null);
        togglePostScrap(postId, loginMemberId, postScrap);
    }

    private void togglePostScrap(Long postId, Long loginMemberId, PostScrap postScrap) {
        if (postScrap != null) {
            deleteScrapIfAlreadyExists(postScrap);
            return;
        }
        saveScrap(postId, loginMemberId);
    }

    private void saveScrap(Long postId, Long loginMemberId) {
        PostScrap postScrap = PostScrap.builder()
                .post(postRepository.getReferenceById(postId))
                .member(memberRepository.getReferenceById(loginMemberId))
                .build();
        postScrapRepository.save(postScrap);
    }

    private void deleteScrapIfAlreadyExists(PostScrap postScrap) {
        postScrapRepository.delete(postScrap);
    }

    @Transactional
    public Long reportPost(Long postId, Long loginMemberId, String content) {
        if (postReportRepository.existsByPostIdAndMemberId(postId, loginMemberId)) {
            throw new PostException(PostErrorInfo.DUPLICATE_REPORT);
        }

        if (postRepository.existsByIdAndMemberId(postId, loginMemberId)) {
            throw new PostException(PostErrorInfo.UNABLE_REPORT_MY_POST);
        }

        PostReport postReport = PostReport.builder()
                .post(postRepository.getReferenceById(postId))
                .member(memberRepository.getReferenceById(loginMemberId))
                .content(content)
                .build();

        return postReportRepository.save(postReport).getId();
    }

    @Transactional
    public Long writePost(Long boardId, Long loginMemberId, PostPostWriteReqDto postPostWriteReqDto) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardException(BoardErrorInfo.NO_BOARD));

        Member loginMember = memberRepository.findById(loginMemberId)
                .orElseThrow(() -> new MemberException(MemberErrorInfo.MEMBER_NOT_FOUND_BY_ID));

        List<ImageInfo> images = postPostWriteReqDto.getImages();

        Post post = Post.builder()
                .board(board)
                .member(loginMember)
                .title(postPostWriteReqDto.getTitle())
                .content(postPostWriteReqDto.getContent())
                .anonymous(postPostWriteReqDto.isAnonymous())
                .build();
        postRepository.save(post);

        if (images.size() > 0) {
            for (ImageInfo image : images) {
                PostImage postImage = PostImage.builder()
                        .post(post)
                        .imagePath(image.getImagePath())
                        .imageUrl(image.getImageUrl())
                        .build();
                postImageRepository.save(postImage);
            }
        }
        return post.getId();
    }

    @Transactional
    public Long deletePost(Long postId, Long loginMemberId) {
        // 1. 게시글 삭제
        Post post = postRepository.findByIdWithMember(postId)
                .orElseThrow(() -> new PostException(PostErrorInfo.NOT_FOUND_POST));

        Member loginMember = memberRepository.findById(loginMemberId)
                .orElseThrow(() -> new MemberException(MemberErrorInfo.MEMBER_NOT_FOUND_BY_ID));

        if (!post.getMember().getId().equals(loginMember.getId())) {
            throw new PostException((PostErrorInfo.UNAUTHORIZED_DELETE_POST));
        }

        postRepository.delete(post);

        // 2. 핫 게시글이 있으면 삭제
        hotPostRepository.findByPostId(postId).ifPresent(hotPostRepository::delete);

        return post.getId();
    }

    @Transactional
    public Long updatePost(Long postId, Long loginMemberId, PostPutUpdateReqDto postPutUpdateReqDto) {
        Post post = postRepository.findWithMemberAndPostImageFetchById(postId)
                .orElseThrow(() -> new PostException(PostErrorInfo.NOT_FOUND_POST));

        Member loginMember = memberRepository.findById(loginMemberId)
                .orElseThrow(() -> new MemberException(MemberErrorInfo.MEMBER_NOT_FOUND_BY_ID));

        if (!post.getMember().getId().equals(loginMember.getId())) {
            throw new PostException(PostErrorInfo.UNAUTHORIZED_UPDATE_POST);
        }

        post.updatePost(postPutUpdateReqDto.getTitle(), postPutUpdateReqDto.getContent(), postPutUpdateReqDto.isAnonymous());
        postImageRepository.deleteAllInBatch(post.getImages());

        List<ImageInfo> images = postPutUpdateReqDto.getImages();
        if (images.size() > 0) {
            for (ImageInfo image : images) {
                PostImage postImage = PostImage.builder()
                        .post(post)
                        .imagePath(image.getImagePath())
                        .imageUrl(image.getImageUrl())
                        .build();
                postImageRepository.save(postImage);
            }
        }
        return post.getId();
    }


    @Transactional(readOnly = true)
    public GetPostHotResDto findHotPosts(Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        List<HotPost> hotPosts = hotPostRepository.findWithDetailsFetch(pageRequest);

        return GetPostHotResDto.from(hotPosts);
    }

    @Transactional(readOnly = true)
    public GetPostMyResDto findMyPosts(Pageable pageable, Long loginMemberId) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        List<Post> posts = postRepository.findWithDetailsByMemberId(loginMemberId, pageRequest);

        return GetPostMyResDto.from(posts);
    }

    @Transactional(readOnly = true)
    public GetPostResDto searchPosts(Long boardId, String keyword, Pageable pageable) {
        if (!boardRepository.existsById(boardId)) {
            throw new BoardException(BoardErrorInfo.NO_BOARD);
        }

        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        List<Post> posts = postRepository.findWithDetailsFetchByBoardIdAndKeyword(boardId, keyword.replaceAll(" ", ""), pageRequest);

        return GetPostResDto.from(posts);
    }

    @Transactional(readOnly = true)
    public GetPostHotResDto searchHotPosts(String keyword, Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        List<HotPost> hotPosts = hotPostRepository.findWithDetailsFetchByKeyword(keyword.replaceAll(" ", ""), pageRequest);
        return GetPostHotResDto.from(hotPosts);
    }
}
