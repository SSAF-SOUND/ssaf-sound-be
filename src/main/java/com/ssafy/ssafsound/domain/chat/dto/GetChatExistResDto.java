package com.ssafy.ssafsound.domain.chat.dto;

import com.ssafy.ssafsound.domain.chat.domain.ChatRoom;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetChatExistResDto {

    private Boolean existence;

    private Long chatRoomId;

    private ChatRoomStatus chatRoomState;

    public static GetChatExistResDto of(ChatRoom chatRoom) {

        GetChatExistResDtoBuilder dtoBuilder = GetChatExistResDto.builder().existence(chatRoom != null);

        if (chatRoom != null) {
            dtoBuilder.chatRoomId(chatRoom.getId()).chatRoomState(chatRoom.getChatRoomStatus());
        }

        return dtoBuilder.build();
    }
}
