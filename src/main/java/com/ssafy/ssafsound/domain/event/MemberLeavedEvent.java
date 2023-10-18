package com.ssafy.ssafsound.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MemberLeavedEvent {

    private Long memberId;
}
