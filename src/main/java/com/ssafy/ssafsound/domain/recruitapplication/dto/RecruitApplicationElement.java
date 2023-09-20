package com.ssafy.ssafsound.domain.recruitapplication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.dto.AuthorElement;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import com.ssafy.ssafsound.domain.recruitapplication.domain.MatchStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruitApplicationElement {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long recruitId;

    @JsonProperty
    private Long recruitApplicationId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String recruitType;

    @JsonProperty
    private MatchStatus matchStatus;

    @JsonProperty
    private AuthorElement author;

    @JsonProperty
    private String reply;

    @JsonProperty
    private String question;

    @JsonProperty
    private Boolean liked;

    @JsonProperty
    private LocalDateTime createdAt;

    @JsonProperty
    private LocalDateTime modifiedAt;

    public RecruitApplicationElement(Long recruitId, Long recruitApplicationId, MetaData recruitType, MatchStatus matchStatus,
                                     Member member,
                                     String reply, String question, Boolean isLike, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.recruitId = recruitId;
        this.recruitApplicationId = recruitApplicationId;
        this.recruitType = recruitType.getName();
        this.matchStatus = matchStatus;
        this.author = new AuthorElement(member, false);
        this.reply = reply;
        this.question = question;
        this.liked = isLike;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
