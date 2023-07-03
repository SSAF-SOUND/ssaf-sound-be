package com.ssafy.ssafsound.domain.chat.domain;

import com.ssafy.ssafsound.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "chat_room")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom extends BaseTimeEntity {

    @Id
    @Column(name = "chat_room_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String sourceType;

    @Column
    private Long sourceId;

    @Enumerated(EnumType.STRING)
    private ChatRoomStatus chatRoomStatus;

    @Column
    private Long initialMemberId;

    @Column
    private Boolean anonymity;
}
