package com.ssafy.ssafsound.domain.recruitcomment.dto;

import com.ssafy.ssafsound.domain.recruitcomment.domain.RecruitComment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostRecruitCommentReqDto {

    @NotBlank
    private String content;

    private Long commentGroup;

    public RecruitComment toEntity() {
        return RecruitComment.builder()
                .content(this.content)
                .deletedComment(false)
                .build();
    }
}
