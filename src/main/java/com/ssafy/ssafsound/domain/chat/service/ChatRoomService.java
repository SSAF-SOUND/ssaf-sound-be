package com.ssafy.ssafsound.domain.chat.service;

import com.ssafy.ssafsound.domain.chat.domain.Chat;
import com.ssafy.ssafsound.domain.chat.domain.ChatRoom;
import com.ssafy.ssafsound.domain.chat.domain.Talker;
import com.ssafy.ssafsound.domain.chat.dto.*;
import com.ssafy.ssafsound.domain.chat.repository.ChatRepository;
import com.ssafy.ssafsound.domain.chat.repository.ChatRoomRepository;
import com.ssafy.ssafsound.domain.chat.repository.TalkerRepository;
import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.member.exception.MemberErrorInfo;
import com.ssafy.ssafsound.domain.member.exception.MemberException;
import com.ssafy.ssafsound.domain.member.repository.MemberRepository;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import com.ssafy.ssafsound.domain.post.repository.PostRepository;
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

    private final PostRepository postRepository;

    private final MetaDataConsumer metaDataConsumer;

    public GetChatRoomsResDto getChatRooms(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorInfo.MEMBER_NOT_FOUND_BY_ID));

        List<Talker> memberAsTalkers = talkerRepository.findAllByMember(member);

        List<ChatRoomElementDto> chatRooms = new ArrayList<>();

        for (Talker memberAsTalker : memberAsTalkers) {

            ChatRoom chatRoom = memberAsTalker.getChatRoom();

            TalkerInfoDto partnerTalkerInfoDto = getPartnerTalkerInfoDtoByAnonymity(memberAsTalker, chatRoom);

            // 채팅 참여자인 talker 의 채팅 시작일 이후 채팅방 전체 메시지 중 가장 최신 메시지
            Chat chat = chatRepository
                    .findFirstByChatRoomCreatedAtAfterOrderByCreatedAtDesc(memberAsTalker.getStartedAt(), chatRoom);

            if (chat == null) {
                continue;
            }

            LatestChatDto latestChatDto = LatestChatDto.from(chat);

            chatRooms.add(ChatRoomElementDto.of(
                    chatRoom,
                    partnerTalkerInfoDto,
                    memberAsTalker,
                    latestChatDto));
        }

        Collections.sort(chatRooms, (dto1, dto2) ->
                dto2.getLatestChatDto().getCreatedAt().compareTo(dto1.getLatestChatDto().getCreatedAt()));

        return GetChatRoomsResDto.builder()
                .chatRooms(chatRooms)
                .build();
    }

    private TalkerInfoDto getPartnerTalkerInfoDtoByAnonymity(Talker memberAsTalker, ChatRoom chatRoom) {

        if (chatRoom.getAnonymity() == null) return null;

        return TalkerInfoDto.from(talkerRepository.findByChatRoomAndIdNot(chatRoom, memberAsTalker.getId()));
    }

}
