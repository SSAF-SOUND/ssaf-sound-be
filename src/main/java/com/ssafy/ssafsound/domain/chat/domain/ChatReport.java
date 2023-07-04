package com.ssafy.ssafsound.domain.chat.domain;

import com.ssafy.ssafsound.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "chat_report")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatReport {

    @Id
    @Column(name = "chat_report_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn
    private Member member;

    @Column
    private String reason;

    @Column
    private LocalDateTime created_at;
}
