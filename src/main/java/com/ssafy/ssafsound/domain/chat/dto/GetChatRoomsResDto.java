package com.ssafy.ssafsound.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GetChatRoomsResDto {

    private List<ChatRoomElementDto> chatRooms;

}
