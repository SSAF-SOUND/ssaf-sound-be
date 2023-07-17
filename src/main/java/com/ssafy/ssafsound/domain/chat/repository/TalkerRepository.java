package com.ssafy.ssafsound.domain.chat.repository;

import com.ssafy.ssafsound.domain.chat.domain.ChatRoom;
import com.ssafy.ssafsound.domain.chat.domain.Talker;
import com.ssafy.ssafsound.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TalkerRepository extends JpaRepository<Talker, Long> {
    List<Talker> findAllByMember(Member member);


    Talker findByChatRoomAndTalkerNot(ChatRoom chatRoom, Talker talker);
}
