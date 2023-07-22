package com.ssafy.ssafsound.domain.chat.domain;

import com.ssafy.ssafsound.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "chat")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Chat extends BaseTimeEntity {

    @Id
    @Column(name = "chat_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "talker_id")
    private Talker talker;

    @Column
    private String message;
}
