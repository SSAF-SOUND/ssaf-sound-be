package com.ssafy.ssafsound.domain.chat.controller;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.auth.validator.Authentication;
import com.ssafy.ssafsound.domain.chat.dto.*;
import com.ssafy.ssafsound.domain.chat.service.ChatRoomService;
import com.ssafy.ssafsound.domain.chat.service.ChatService;
import com.ssafy.ssafsound.global.common.response.EnvelopeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    private final ChatRoomService chatRoomService;

    @GetMapping("/rooms")
    public EnvelopeResponse<GetChatRoomsResDto> getChatRooms(@Authentication AuthenticatedMember member) {

        return EnvelopeResponse.<GetChatRoomsResDto>builder()
                .data(chatRoomService.getChatRooms(member.getMemberId()))
                .build();
    }

    @PatchMapping("/{chatRoomId}")
    public EnvelopeResponse<PatchChatResDto> readChatRoom(@Authentication AuthenticatedMember member,
                                                          @PathVariable Long chatRoomId, Pageable pageable) {

        return EnvelopeResponse.<PatchChatResDto>builder()
                .data(chatService.readChatRoom(member.getMemberId(), chatRoomId, pageable))
                .build();
    }

}
