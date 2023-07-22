package com.ssafy.ssafsound.domain.chat.domain;

import com.ssafy.ssafsound.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "talker")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Talker {

    @Id
    @Column(name = "talker_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    private LocalDateTime startedAt;

    @Column
    private LocalDateTime readAt;

    public void setStartedAt(LocalDateTime inputTime) {
        this.startedAt = inputTime;
    }
}
