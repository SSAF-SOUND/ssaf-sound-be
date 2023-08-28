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
import com.ssafy.ssafsound.infra.exception.InfraException;
import com.ssafy.ssafsound.infra.storage.service.AwsS3StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final HotPostRepository hotPostRepository;
    private final PostScrapRepository postScrapRepository;
    private final PostImageRepository postImageRepository;
    private final AwsS3StorageService awsS3StorageService;
    private final PostConstantProvider postConstantProvider;

    @Transactional(readOnly = true)
    public GetPostResDto findPosts(GetPostReqDto getPostReqDto) {
        Long boardId = getPostReqDto.getBoardId();
        Long cursor = getPostReqDto.getCursor();
        int size = getPostReqDto.getSize();

        if (!boardRepository.existsById(boardId)) {
            throw new BoardException(BoardErrorInfo.NO_BOARD);
        }

        List<Post> posts = postRepository.findWithDetailsByBoardId(boardId, cursor, size);
        return GetPostResDto.ofPosts(posts, size);
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
    public PostCommonLikeResDto likePost(Long postId, Long loginMemberId) {
        Member loginMember = memberRepository.findById(loginMemberId)
                .orElseThrow(() -> new MemberException(MemberErrorInfo.MEMBER_NOT_FOUND_BY_ID));

        PostLike postLike = postLikeRepository.findByPostIdAndMemberId(postId, loginMember.getId())
                .orElse(null);

        Integer likeCount = postLikeRepository.countByPostId(postId);
        return togglePostLike(likeCount, postId, loginMember, postLike);
    }

    private PostCommonLikeResDto togglePostLike(Integer likeCount, Long postId, Member loginMember, PostLike postLike) {
        if (postLike != null) {
            deleteLike(postLike);
            return new PostCommonLikeResDto(likeCount - 1, false);
        }

        saveLike(postId, loginMember);
        if (isSelectedHotPost(postId)) {
            saveHotPost(postId);
        }
        return new PostCommonLikeResDto(likeCount + 1, true);
    }

    private void saveLike(Long postId, Member loginMember) {
        PostLike postLike = PostLike.builder()
                .post(postRepository.getReferenceById(postId))
                .member(loginMember)
                .build();
        postLikeRepository.save(postLike);
    }

    private void deleteLike(PostLike postLike) {
        postLikeRepository.delete(postLike);
    }


    private boolean isSelectedHotPost(Long postId) {
        return postLikeRepository.countByPostId(postId) >= postConstantProvider.getHOT_POST_LIKES_THRESHOLD();
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
    public PostPostScrapResDto scrapPost(Long postId, Long loginMemberId) {
        Member loginMember = memberRepository.findById(loginMemberId)
                .orElseThrow(() -> new MemberException(MemberErrorInfo.MEMBER_NOT_FOUND_BY_ID));

        PostScrap postScrap = postScrapRepository.findByPostIdAndMemberId(postId, loginMember.getId())
                .orElse(null);

        Integer scrapCount = postScrapRepository.countByPostId(postId);
        return togglePostScrap(scrapCount, postId, loginMember, postScrap);
    }

    private PostPostScrapResDto togglePostScrap(Integer scrapCount, Long postId, Member loginMember, PostScrap postScrap) {
        if (postScrap != null) {
            deleteScrapIfAlreadyExists(postScrap);
            return new PostPostScrapResDto(scrapCount - 1, false);
        }
        saveScrap(postId, loginMember);
        return new PostPostScrapResDto(scrapCount + 1, true);
    }

    private void saveScrap(Long postId, Member loginMember) {
        PostScrap postScrap = PostScrap.builder()
                .post(postRepository.getReferenceById(postId))
                .member(loginMember)
                .build();
        postScrapRepository.save(postScrap);
    }

    private void deleteScrapIfAlreadyExists(PostScrap postScrap) {
        postScrapRepository.delete(postScrap);
    }

    @Transactional
    public PostIdElement writePost(Long boardId, Long loginMemberId, PostPostWriteReqDto postPostWriteReqDto) {
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
                .anonymity(postPostWriteReqDto.isAnonymity())
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
        return new PostIdElement(post.getId());
    }

    @Transactional
    public PostIdElement deletePost(Long postId, Long loginMemberId) {
        Post post = postRepository.findByIdWithMember(postId)
                .orElseThrow(() -> new PostException(PostErrorInfo.NOT_FOUND_POST));

        Member loginMember = memberRepository.findById(loginMemberId)
                .orElseThrow(() -> new MemberException(MemberErrorInfo.MEMBER_NOT_FOUND_BY_ID));

        if (!post.getMember().getId().equals(loginMember.getId())) {
            throw new PostException((PostErrorInfo.UNAUTHORIZED_DELETE_POST));
        }

        deleteAllPostImages(post.getImages());
        postRepository.delete(post);
        hotPostRepository.findByPostId(postId).ifPresent(hotPostRepository::delete);
        return new PostIdElement(postId);
    }

    private void deleteAllPostImages(List<PostImage> images) {
        images.forEach(image -> {
            try {
                awsS3StorageService.deleteObject(image);

            } catch (InfraException e) {
                log.error("이미지 삭제에 오류가 발생했습니다.");
            }
            postImageRepository.delete(image);
        });
    }

    @Transactional
    public PostIdElement updatePost(Long postId, Long loginMemberId, PostPatchUpdateReqDto postPatchUpdateReqDto) {
        Post post = postRepository.findWithMemberAndPostImageFetchById(postId)
                .orElseThrow(() -> new PostException(PostErrorInfo.NOT_FOUND_POST));

        Member loginMember = memberRepository.findById(loginMemberId)
                .orElseThrow(() -> new MemberException(MemberErrorInfo.MEMBER_NOT_FOUND_BY_ID));

        if (!post.getMember().getId().equals(loginMember.getId())) {
            throw new PostException(PostErrorInfo.UNAUTHORIZED_UPDATE_POST);
        }

        post.updatePost(postPatchUpdateReqDto.getTitle(), postPatchUpdateReqDto.getContent(), postPatchUpdateReqDto.isAnonymity());
        postImageRepository.deleteAllInBatch(post.getImages());

        List<ImageInfo> images = postPatchUpdateReqDto.getImages();
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
        return new PostIdElement(post.getId());
    }


    @Transactional(readOnly = true)
    public GetPostResDto findHotPosts(GetPostHotReqDto getPostHotReqDto) {
        Long cursor = getPostHotReqDto.getCursor();
        int size = getPostHotReqDto.getSize();

        List<HotPost> hotPosts = hotPostRepository.findHotPosts(cursor, size);
        return GetPostResDto.ofHotPosts(hotPosts, size);
    }

    @Transactional(readOnly = true)
    public GetPostResDto findMyPosts(GetPostMyReqDto getPostMyReqDto, Long loginMemberId) {
        Long cursor = getPostMyReqDto.getCursor();
        int size = getPostMyReqDto.getSize();

        Member loginMember = memberRepository.findById(loginMemberId)
                .orElseThrow(() -> new MemberException(MemberErrorInfo.MEMBER_NOT_FOUND_BY_ID));

        List<Post> posts = postRepository.findWithDetailsByMemberId(loginMember.getId(), cursor, size);
        return GetPostResDto.ofPosts(posts, size);
    }

    @Transactional(readOnly = true)
    public GetPostResDto findMyScrapPosts(GetPostMyReqDto getPostMyScrapReqDto, Long loginMemberId) {
        Long cursor = getPostMyScrapReqDto.getCursor();
        int size = getPostMyScrapReqDto.getSize();

        Member loginMember = memberRepository.findById(loginMemberId)
                .orElseThrow(() -> new MemberException(MemberErrorInfo.MEMBER_NOT_FOUND_BY_ID));

        List<PostScrap> postScraps = postScrapRepository.findMyScrapPosts(loginMember.getId(), cursor, size);
        return GetPostResDto.ofPostScraps(postScraps, size);
    }

    @Transactional(readOnly = true)
    public GetPostResDto searchPosts(GetPostSearchReqDto getPostSearchReqDto) {
        Long boardId = getPostSearchReqDto.getBoardId();
        String keyword = getPostSearchReqDto.getKeyword();
        Long cursor = getPostSearchReqDto.getCursor();
        int size = getPostSearchReqDto.getSize();

        if (!boardRepository.existsById(boardId)) {
            throw new BoardException(BoardErrorInfo.NO_BOARD);
        }

        List<Post> posts = postRepository.findWithDetailsFetchByBoardIdAndKeyword(boardId, keyword.replaceAll(" ", ""), cursor, size);
        return GetPostResDto.ofPosts(posts, size);
    }

    @Transactional(readOnly = true)
    public GetPostResDto searchHotPosts(GetPostHotSearchReqDto getPostHotSearchReqDto) {
        String keyword = getPostHotSearchReqDto.getKeyword();
        Long cursor = getPostHotSearchReqDto.getCursor();
        int size = getPostHotSearchReqDto.getSize();

        List<HotPost> hotPosts = hotPostRepository.findHotPostsByKeyword(keyword.replaceAll(" ", ""), cursor, size);
        return GetPostResDto.ofHotPosts(hotPosts, size);
    }
}
