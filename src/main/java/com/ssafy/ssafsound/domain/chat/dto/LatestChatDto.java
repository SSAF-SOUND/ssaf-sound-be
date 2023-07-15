package com.ssafy.ssafsound.domain.chat.dto;

import com.ssafy.ssafsound.domain.chat.domain.Chat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class LatestChatDto {

    private String message;

    private LocalDateTime createdAt;

    public static LatestChatDto from(Chat chat) {

        String message = chat.getMessage();

        if (message.length() > 20) message.substring(0, 20);

        return LatestChatDto.builder()
                .message(message)
                .createdAt(chat.getCreatedAt())
                .build();
    }

}
