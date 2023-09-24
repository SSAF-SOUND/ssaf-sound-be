package com.ssafy.ssafsound.domain.recruitapplication.dto;

import com.ssafy.ssafsound.domain.member.dto.AuthorElement;
import com.ssafy.ssafsound.domain.recruitapplication.domain.MatchStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RecruitApplicationResElement {
    private Long recruitApplicationId;
    private MatchStatus matchStatus;
    private AuthorElement author;
    private String reply;
    private String question;
    private Boolean liked;
    private LocalDateTime appliedAt;

    public RecruitApplicationResElement(RecruitApplicationElement recruitApplicationElement) {
        this.recruitApplicationId = recruitApplicationElement.getRecruitApplicationId();
        this.matchStatus =  recruitApplicationElement.getMatchStatus();
        this.author =  recruitApplicationElement.getAuthor();
        this.reply = recruitApplicationElement.getReply();
        this.question =  recruitApplicationElement.getQuestion();
        this.liked =  recruitApplicationElement.getLiked();
        this.appliedAt = recruitApplicationElement.getCreatedAt();
    }
}
