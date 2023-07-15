package com.ssafy.ssafsound.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatRoomElementDto {

    private Long chatRoomId;

    private Boolean anonymous;

    @JsonProperty("talker")
    private TalkerInfoDto talkerInfoDto;

    private LocalDateTime readAt;

    @JsonProperty("latestChat")
    private LatestChatDto latestChatDto;
}
