package com.ssafy.ssafsound.domain.recruitapplication.dto;

import com.ssafy.ssafsound.domain.member.dto.AuthorElement;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetRecruitApplicationDetailResDto {
    private Long recruitId;
    private Long recruitApplicationId;
    private String recruitType;
    private String matchStatus;
    private AuthorElement author;
    private String reply;
    private String question;
    private Boolean liked;

    public GetRecruitApplicationDetailResDto(RecruitApplicationElement recruitApplicationElement) {
        this.recruitId = recruitApplicationElement.getRecruitId();
        this.recruitApplicationId = recruitApplicationElement.getRecruitApplicationId();
        this.recruitType = recruitApplicationElement.getRecruitType();
        this.matchStatus = recruitApplicationElement.getMatchStatus().name();
        this.author = recruitApplicationElement.getAuthor();
        this.reply = recruitApplicationElement.getReply();
        this.question = recruitApplicationElement.getQuestion();
        this.liked = recruitApplicationElement.getLiked();
    }
}
