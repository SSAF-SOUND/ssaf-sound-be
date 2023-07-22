package com.ssafy.ssafsound.domain.chat.service;

import com.ssafy.ssafsound.domain.chat.dto.*;
import com.ssafy.ssafsound.domain.chat.repository.ChatRepository;
import com.ssafy.ssafsound.domain.chat.repository.ChatRoomRepository;
import com.ssafy.ssafsound.domain.chat.repository.TalkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final TalkerRepository talkerRepository;

    public PatchChatResDto readChatRoom(Long memberId, Long chatRoomId, Pageable pageable) {
        return null;
    }
}
