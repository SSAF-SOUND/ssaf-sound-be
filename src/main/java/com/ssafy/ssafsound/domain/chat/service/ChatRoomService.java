package com.ssafy.ssafsound.domain.chat.service;

import com.ssafy.ssafsound.domain.chat.dto.*;
import com.ssafy.ssafsound.domain.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public GetChatRoomsResDto getChatRooms(Long memberId, GetChatRoomsReqDto getChatRoomsReqDto) {
        return null;
    }

    public GetChatExistResDto getChatExistence(Long memberId, GetChatExistReqDto getChatExistReqDto) {
        return null;
    }


}
