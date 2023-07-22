package com.ssafy.ssafsound.domain.chat.repository;

import com.ssafy.ssafsound.domain.chat.domain.Chat;
import com.ssafy.ssafsound.domain.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    Chat findFirstByChatRoomCreatedAtAfterOrderByCreatedAtDesc(LocalDateTime startedAt,ChatRoom chatRoom);
}
