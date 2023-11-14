package com.ssafy.ssafsound.domain.notification.dto;

import lombok.*;

import javax.validation.constraints.Min;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetNotificationReqDto {
    @Builder.Default
    private Long cursor = -1L;

    @Min(value = 10, message = "Size가 너무 작습니다.")
    @Builder.Default
    private Integer size = 10;

    public void setCursor(Long cursor) {
        if (cursor <= -1) {
            this.cursor = -1L;
            return;
        }
        this.cursor = cursor;
    }
}
