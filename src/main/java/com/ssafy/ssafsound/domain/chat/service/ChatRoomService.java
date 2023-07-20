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
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import com.ssafy.ssafsound.domain.meta.domain.MetaDataType;
import com.ssafy.ssafsound.domain.meta.domain.SourceType;
import com.ssafy.ssafsound.domain.meta.exception.MetaDataIntegrityException;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import com.ssafy.ssafsound.domain.post.exception.PostErrorInfo;
import com.ssafy.ssafsound.domain.post.exception.PostException;
import com.ssafy.ssafsound.domain.post.repository.PostRepository;
import com.ssafy.ssafsound.global.common.exception.GlobalErrorInfo;
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

            if (chatRoom.getChatRoomStatus().equals(ChatRoomStatus.TERMINATED)) continue;

            TalkerInfoDto partnerTalkerInfoDto =
                    chatRoom.getAnonymity() ? null : TalkerInfoDto.from(
                            talkerRepository.findByChatRoomAndIdNot(chatRoom, memberAsTalker.getId()));

            chatRooms.add(ChatRoomElementDto.of(
                    chatRoom,
                    partnerTalkerInfoDto,
                    memberAsTalker,
                    chatRepository.findFirstByChatRoomOrderByCreatedAtDesc(chatRoom)));
        }

        Collections.sort(chatRooms, (dto1, dto2) ->
                dto2.getLatestChatDto().getCreatedAt().compareTo(dto1.getLatestChatDto().getCreatedAt()));

        return GetChatRoomsResDto.builder()
                .chatRooms(chatRooms)
                .build();
    }

    public GetChatExistResDto getChatExistence(Long memberId, GetChatExistReqDto getChatExistReqDto) {

        memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorInfo.MEMBER_NOT_FOUND_BY_ID));

        MetaData requestedSourceType = metaDataConsumer.getMetaData(MetaDataType.SOURCE_TYPE.name(),
                getChatExistReqDto.getSourceType());

        MetaData chatEnableSourceType = metaDataConsumer.getMetaData(MetaDataType.SOURCE_TYPE.name(), SourceType.POST.getName());

        if (!requestedSourceType.equals(chatEnableSourceType)) throw new MetaDataIntegrityException(GlobalErrorInfo.BAD_REQUEST);

        postRepository.findById(getChatExistReqDto.getSourceId())
                .orElseThrow(() -> new PostException(PostErrorInfo.NOT_FOUND_POST));

        return GetChatExistResDto.of(chatRoomRepository.findBySourceTypeAndSourceIdAndInitialMemberId(
                requestedSourceType,
                getChatExistReqDto.getSourceId(),
                memberId));
    }

}
