package com.ssafy.ssafsound.domain.chat.repository;

import com.ssafy.ssafsound.domain.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
