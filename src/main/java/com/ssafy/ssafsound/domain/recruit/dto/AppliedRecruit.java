package com.ssafy.ssafsound.domain.recruit.dto;

import com.ssafy.ssafsound.domain.recruit.domain.Recruit;
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
public class AppliedRecruit {
    private Recruit recruit;
    private MatchStatus matchStatus;
    private LocalDateTime appliedAt;
}
