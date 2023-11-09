package com.ssafy.ssafsound.domain.comment.service;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.comment.domain.Comment;
import com.ssafy.ssafsound.domain.comment.domain.CommentLike;
import com.ssafy.ssafsound.domain.comment.domain.CommentNumber;
import com.ssafy.ssafsound.domain.comment.dto.CommentIdElement;
import com.ssafy.ssafsound.domain.comment.dto.GetCommentResDto;
import com.ssafy.ssafsound.domain.comment.dto.PatchCommentUpdateReqDto;
import com.ssafy.ssafsound.domain.comment.dto.PostCommentWriteReqDto;
import com.ssafy.ssafsound.domain.comment.exception.CommentErrorInfo;
import com.ssafy.ssafsound.domain.comment.exception.CommentException;
import com.ssafy.ssafsound.domain.comment.repository.CommentLikeRepository;
import com.ssafy.ssafsound.domain.comment.repository.CommentNumberRepository;
import com.ssafy.ssafsound.domain.comment.repository.CommentRepository;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.exception.MemberErrorInfo;
import com.ssafy.ssafsound.domain.member.exception.MemberException;
import com.ssafy.ssafsound.domain.member.repository.MemberRepository;
import com.ssafy.ssafsound.domain.notification.dto.CreateNotification;
import com.ssafy.ssafsound.domain.notification.service.NotificationService;
import com.ssafy.ssafsound.domain.post.domain.Post;
import com.ssafy.ssafsound.domain.post.dto.PostCommonLikeResDto;
import com.ssafy.ssafsound.domain.post.exception.PostErrorInfo;
import com.ssafy.ssafsound.domain.post.exception.PostException;
import com.ssafy.ssafsound.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentNumberRepository commentNumberRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;


    @Transactional
    public CommentIdElement writeComment(Long postId, Long loginMemberId, PostCommentWriteReqDto postCommentWriteReqDto) {
        Post post = postRepository.findByIdWithMember(postId)
                .orElseThrow(() -> new PostException(PostErrorInfo.NOT_FOUND_POST));

        Member loginMember = memberRepository.findById(loginMemberId)
                .orElseThrow(() -> new MemberException(MemberErrorInfo.MEMBER_NOT_FOUND_BY_ID));

        // 1. 익명 번호 부여
        CommentNumber commentNumber = commentNumberRepository.
                findByPostIdAndMemberId(postId, loginMemberId).orElse(null);

        if (commentNumber == null) {
            commentNumber = CommentNumber.builder()
                    .post(postRepository.getReferenceById(postId))
                    .member(loginMember)
                    .number(commentNumberRepository.countAllByPostId(postId) + 1)
                    .build();
            commentNumberRepository.save(commentNumber);
        }

        // 2. 댓글 저장
        Comment comment = Comment.builder()
                .post(postRepository.getReferenceById(postId))
                .member(memberRepository.getReferenceById(loginMemberId))
                .content(postCommentWriteReqDto.getContent())
                .anonymity(postCommentWriteReqDto.getAnonymity())
                .commentNumber(commentNumber)
                .commentGroup(null)
                .build();

        comment = commentRepository.save(comment);
        commentRepository.updateByCommentGroup(comment.getId());

        if (!post.isMine(loginMember)) {
            notificationService.sendNotification(CreateNotification.postReplyNotificationFrom(post));
        }

        return new CommentIdElement(comment.getId());
    }

    @Transactional(readOnly = true)
    public GetCommentResDto findComments(Long postId, AuthenticatedMember loginMember) {
        if (!postRepository.existsById(postId)) {
            throw new PostException(PostErrorInfo.NOT_FOUND_POST);
        }

        List<Comment> comments = commentRepository.findAllPostIdWithDetailsFetchOrderByCommentGroupId(postId);
        return GetCommentResDto.of(comments, loginMember);
    }

    @Transactional
    public CommentIdElement updateComment(Long commentId, Long loginMemberId, PatchCommentUpdateReqDto patchCommentUpdateReqDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(CommentErrorInfo.NOT_FOUND_COMMENT));

        Member loginMember = memberRepository.findById(loginMemberId)
                .orElseThrow(() -> new MemberException(MemberErrorInfo.MEMBER_NOT_FOUND_BY_ID));

        if (!comment.getMember().getId().equals(loginMember.getId())) {
            throw new CommentException(CommentErrorInfo.UNAUTHORIZED_UPDATE_COMMENT);
        }

        comment.updateComment(patchCommentUpdateReqDto.getContent(), patchCommentUpdateReqDto.getAnonymity());
        return new CommentIdElement(comment.getId());
    }

    @Transactional
    public CommentIdElement writeCommentReply(Long postId, Long commentId, Long loginMemberId, PostCommentWriteReqDto postCommentWriteReplyReqDto) {
        if (!postRepository.existsById(postId)) {
            throw new PostException(PostErrorInfo.NOT_FOUND_POST);
        }

        if (!commentRepository.existsById(commentId)) {
            throw new CommentException(CommentErrorInfo.NOT_FOUND_COMMENT);
        }

        Member loginMember = memberRepository.findById(loginMemberId)
                .orElseThrow(() -> new MemberException(MemberErrorInfo.MEMBER_NOT_FOUND_BY_ID));

        // 1. 익명 번호 부여
        CommentNumber commentNumber = commentNumberRepository.
                findByPostIdAndMemberId(postId, loginMemberId).orElse(null);

        if (commentNumber == null) {
            commentNumber = CommentNumber.builder()
                    .post(postRepository.getReferenceById(postId))
                    .member(loginMember)
                    .number(commentNumberRepository.countAllByPostId(postId) + 1)
                    .build();
            commentNumberRepository.save(commentNumber);
        }

        // 2. 대댓글 저장
        Comment comment = Comment.builder()
                .post(postRepository.getReferenceById(postId))
                .member(loginMember)
                .content(postCommentWriteReplyReqDto.getContent())
                .anonymity(postCommentWriteReplyReqDto.getAnonymity())
                .commentNumber(commentNumber)
                .commentGroup(commentRepository.getReferenceById(commentId))
                .build();

        comment = commentRepository.save(comment);
        return new CommentIdElement(comment.getId());
    }

    @Transactional
    public PostCommonLikeResDto likeComment(Long commentId, Long loginMemberId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(CommentErrorInfo.NOT_FOUND_COMMENT));

        Member loginMember = memberRepository.findById(loginMemberId)
                .orElseThrow(() -> new MemberException(MemberErrorInfo.MEMBER_NOT_FOUND_BY_ID));

        CommentLike commentLike = commentLikeRepository.findByCommentIdAndMemberId(commentId, loginMember.getId())
                .orElse(null);

        Integer likeCount = commentLikeRepository.countByCommentId(commentId);

        return toggleCommentLike(likeCount, comment, loginMember, commentLike);
    }

    private PostCommonLikeResDto toggleCommentLike(Integer likeCount, Comment comment, Member loginMember, CommentLike commentLike) {
        if (commentLike != null) {
            deleteCommentLike(commentLike);
            return new PostCommonLikeResDto(likeCount - 1, false);
        }
        saveCommentLike(comment, loginMember);
        return new PostCommonLikeResDto(likeCount + 1, true);
    }

    private void saveCommentLike(Comment comment, Member loginMember) {
        CommentLike commentLike = CommentLike.builder()
                .member(loginMember)
                .comment(comment)
                .build();
        commentLikeRepository.save(commentLike);
    }

    private void deleteCommentLike(CommentLike commentLike) {
        commentLikeRepository.delete(commentLike);
    }

    @Transactional
    public CommentIdElement deleteComment(Long commentId, Long loginMemberId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(CommentErrorInfo.NOT_FOUND_COMMENT));

        Member member = memberRepository.findById(loginMemberId)
                .orElseThrow(() -> new MemberException(MemberErrorInfo.MEMBER_NOT_FOUND_BY_ID));

        if (!comment.getMember().getId().equals(member.getId())) {
            throw new CommentException(CommentErrorInfo.UNAUTHORIZED_DELETE_COMMENT);
        }

        commentRepository.delete(comment);
        return new CommentIdElement(comment.getId());
    }
}
