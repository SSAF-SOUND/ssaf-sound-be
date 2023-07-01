package com.ssafy.ssafsound.domain.comment.service;

import com.ssafy.ssafsound.domain.comment.domain.Comment;
import com.ssafy.ssafsound.domain.comment.domain.CommentNumber;
import com.ssafy.ssafsound.domain.comment.dto.PostCommentWriteReqDto;
import com.ssafy.ssafsound.domain.comment.exception.CommentErrorInfo;
import com.ssafy.ssafsound.domain.comment.exception.CommentException;
import com.ssafy.ssafsound.domain.comment.repository.CommentNumberRepository;
import com.ssafy.ssafsound.domain.comment.repository.CommentRepository;
import com.ssafy.ssafsound.domain.member.repository.MemberRepository;
import com.ssafy.ssafsound.domain.post.exception.PostErrorInfo;
import com.ssafy.ssafsound.domain.post.exception.PostException;
import com.ssafy.ssafsound.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentNumberRepository commentNumberRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;


    @Transactional
    public Long writeComment(Long postId, Long memberId, PostCommentWriteReqDto postCommentWriteReqDto) {
        if (!postRepository.existsById(postId)) {
            throw new PostException(PostErrorInfo.NOT_FOUND);
        }

        // 1. 익명 번호 부여
        CommentNumber commentNumber = null;
        if (!commentNumberRepository.existsByPostIdAndMemberId(postId, memberId)) {
            commentNumber = CommentNumber.builder()
                    .post(postRepository.getReferenceById(postId))
                    .member(memberRepository.getReferenceById(memberId))
                    .number(commentNumberRepository.countAllByPostId(postId) + 1)
                    .build();
            commentNumberRepository.save(commentNumber);
        }

        // 2. 댓글 저장
        if (commentNumber == null) {
            commentNumber = commentNumberRepository
                    .findByPostIdAndMemberId(postId, memberId)
                    .orElseThrow(() -> new CommentException(CommentErrorInfo.NOT_FOUND_COMMENT_NUMBER));
        }

        Comment comment = Comment.builder()
                .post(postRepository.getReferenceById(postId))
                .member(memberRepository.getReferenceById(memberId))
                .content(postCommentWriteReqDto.getContent())
                .parent(true)
                .anonymous(postCommentWriteReqDto.getAnonymous())
                .commentNumber(commentNumber)
                .commentGroup(null)
                .build();

        return commentRepository.save(comment).getId();
    }

}
