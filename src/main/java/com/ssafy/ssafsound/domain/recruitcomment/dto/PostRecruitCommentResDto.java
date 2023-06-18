package com.ssafy.ssafsound.domain.recruitcomment.dto;

import com.ssafy.ssafsound.domain.recruitcomment.domain.RecruitComment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostRecruitCommentResDto {
    private Long recruitCommentId;
    private String content;
    private Long memberId;
    private String nickName;
    private Long commentGroup;

    public static PostRecruitCommentResDto from(RecruitComment recruitComment) {
        return new PostRecruitCommentResDto(
                recruitComment.getId(),
                recruitComment.getContent(),
                recruitComment.getMember().getId(),
                recruitComment.getMember().getNickname(),
                recruitComment.getCommentGroup().getId()
        );
    }
}
