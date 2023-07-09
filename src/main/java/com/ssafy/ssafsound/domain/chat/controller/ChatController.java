package com.ssafy.ssafsound.domain.chat.controller;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.auth.validator.Authentication;
import com.ssafy.ssafsound.domain.chat.dto.*;
import com.ssafy.ssafsound.domain.chat.service.ChatService;
import com.ssafy.ssafsound.global.common.response.EnvelopeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping
    public EnvelopeResponse<GetChatRoomsResDto> getChatRooms(@Authentication AuthenticatedMember member,
                                                             GetChatRoomsReqDto getChatRoomsReqDto) {

        return EnvelopeResponse.<GetChatRoomsResDto>builder()
                .data(chatService.getChatRooms(member.getMemberId(), getChatRoomsReqDto))
                .build();
    }

    @GetMapping("/check")
    public EnvelopeResponse<GetChatExistResDto> getChatExistence(@Authentication AuthenticatedMember member,
                                                                 @Valid GetChatExistReqDto getChatExistReqDto) {

        return EnvelopeResponse.<GetChatExistResDto>builder()
                .data(chatService.getChatExistence(member.getMemberId(), getChatExistReqDto))
                .build();
    }

    @PatchMapping("/{chatRoomId}")
    public EnvelopeResponse<PatchChatResDto> patchChat(@Authentication AuthenticatedMember member,
                                                       PatchChatReqDto patchChatReqDto, Pageable pageable) {

        return EnvelopeResponse.<PatchChatResDto>builder()
                .data(chatService.readChatRoom(member.getMemberId(), patchChatReqDto, pageable))
                .build();
    }

}
