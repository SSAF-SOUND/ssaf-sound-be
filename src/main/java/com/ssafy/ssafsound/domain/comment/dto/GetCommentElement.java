package com.ssafy.ssafsound.domain.comment.dto;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.comment.domain.Comment;
import com.ssafy.ssafsound.domain.comment.domain.CommentLike;
import com.ssafy.ssafsound.domain.member.domain.MemberRole;
import com.ssafy.ssafsound.domain.member.dto.SSAFYInfo;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class GetCommentElement {
    private String content;
    private int likeCount;
    private LocalDateTime createdAt;
    private Boolean anonymous;
    private Boolean modified;
    private Boolean liked;
    private Boolean mine;
    private Boolean deletedComment;
    private AuthorElement author;
//    private String nickname;
//    private final MemberRole memberRole;
//    private final Boolean ssafyMember;
//    private final Boolean isMajor;
//    private final SSAFYInfo ssafyInfo;

    private List<GetCommentElement> replies;


    public static GetCommentElement of(Comment comment, AuthenticatedMember loginMember) {
        Boolean anonymous = comment.getAnonymous();

        return GetCommentElement.builder()
                .content(comment.getDeletedComment() ? "삭제된 댓글입니다." : comment.getContent())
                .likeCount(comment.getLikes().size())
                .createdAt(comment.getCreatedAt())
//                .nickname(setNickName(comment, anonymous))
                .anonymous(anonymous)
                .modified(comment.getModifiedAt() != null)
                .liked(isLiked(comment, loginMember))
                .mine(isMine(comment, loginMember))
                .deletedComment(comment.getDeletedComment())
                .author(AuthorElement.from(comment))
                .replies(new ArrayList<>())
//                .memberRole(comment.getMember().getRole())
//                .ssafyMember(comment.getMember().getSsafyMember())
//                .isMajor(comment.getMember().getMajor())
//                .ssafyInfo(SSAFYInfo.from(comment.getMember()))
                .build();
    }

//    private static String setNickName(Comment comment, Boolean anonymous) {
//        if (anonymous)
//            return "익명 " + comment.getCommentNumber().getNumber();
//        return comment.getMember().getNickname();
//    }

    private static Boolean isLiked(Comment comment, AuthenticatedMember loginMember) {
        if (loginMember == null)
            return false;

        Long loginMemberId = loginMember.getMemberId();
        for (CommentLike like : comment.getLikes()) {
            if (like.getMember().getId().equals(loginMemberId))
                return true;
        }
        return false;
    }

    private static Boolean isMine(Comment comment, AuthenticatedMember loginMember) {
        if (loginMember == null)
            return false;

        return comment.getMember().getId().equals(loginMember.getMemberId());
    }

    public void addReply(GetCommentElement reply) {
        this.replies.add(reply);
    }
}
