package com.ssafy.ssafsound.domain.chat.service;

import com.ssafy.ssafsound.domain.chat.domain.ChatRoom;
import com.ssafy.ssafsound.domain.chat.domain.ChatRoomStatus;
import com.ssafy.ssafsound.domain.chat.domain.Talker;
import com.ssafy.ssafsound.domain.chat.dto.*;
import com.ssafy.ssafsound.domain.chat.repository.ChatRepository;
import com.ssafy.ssafsound.domain.chat.repository.ChatRoomRepository;
import com.ssafy.ssafsound.domain.chat.repository.TalkerRepository;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.exception.MemberErrorInfo;
import com.ssafy.ssafsound.domain.member.exception.MemberException;
import com.ssafy.ssafsound.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    private final ChatRepository chatRepository;

    private final TalkerRepository talkerRepository;

    private final MemberRepository memberRepository;

    public GetChatRoomsResDto getChatRooms(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorInfo.MEMBER_NOT_FOUND_BY_ID));

        List<Talker> memberAsTalkers = talkerRepository.findAllByMember(member);

        List<ChatRoomElementDto> chatRooms = new ArrayList<>();

        for (Talker memberAsTalker : memberAsTalkers) {

            ChatRoom chatRoom = memberAsTalker.getChatRoom();

            if (chatRoom.getChatRoomStatus().equals(ChatRoomStatus.TERMINATED)) continue;

            TalkerInfoDto partnerTalkerInfoDto =
                    chatRoom.getAnonymity() ? null : TalkerInfoDto.from(
                            talkerRepository.findByChatRoomAndNotTalker(chatRoom, memberAsTalker));

            chatRooms.add(ChatRoomElementDto.of(
                    chatRoom,
                    partnerTalkerInfoDto,
                    memberAsTalker,
                    chatRepository.findFirstByChatRoomOrderByCreatAtDesc(chatRoom)));
        }

        Collections.sort(chatRooms, (dto1, dto2) ->
                dto2.getLatestChatDto().getCreatedAt().compareTo(dto1.getLatestChatDto().getCreatedAt()));

        return GetChatRoomsResDto.builder()
                .chatRooms(chatRooms)
                .build();
    }

    public GetChatExistResDto getChatExistence(Long memberId, GetChatExistReqDto getChatExistReqDto) {
        return null;
    }


}
