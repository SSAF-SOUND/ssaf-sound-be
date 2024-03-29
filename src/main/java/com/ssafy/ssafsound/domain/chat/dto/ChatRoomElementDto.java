package com.ssafy.ssafsound.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.ssafsound.domain.chat.domain.ChatRoom;
import com.ssafy.ssafsound.domain.chat.domain.Talker;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatRoomElementDto {

    private Long chatRoomId;

    private Boolean anonymity;

    @JsonProperty("talker")
    private TalkerInfoDto talkerInfoDto;

    @JsonProperty("latestChat")
    private LatestChatDto latestChatDto;

    private LocalDateTime readAt;

    public static ChatRoomElementDto of(ChatRoom chatRoom, TalkerInfoDto partnerTalkerInfoDto,
                                        Talker talker, LatestChatDto latestChatDto) {

        return ChatRoomElementDto.builder()
                .chatRoomId(chatRoom.getId())
                .anonymity(chatRoom.getAnonymity())
                .talkerInfoDto(partnerTalkerInfoDto)
                .latestChatDto(latestChatDto)
                .readAt(talker.getReadAt())
                .build();
    }

}
