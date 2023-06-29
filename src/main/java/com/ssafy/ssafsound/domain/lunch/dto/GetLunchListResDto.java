package com.ssafy.ssafsound.domain.lunch.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class GetLunchListResDto {

    @Builder.Default
    private Long totalPollCount = 0L;

    @Builder.Default
    private Long polledAt = -1L;

    @Builder.Default
    private List<GetLunchListElementResDto> menus = new ArrayList<>();

    public static GetLunchListResDto of(List<GetLunchListElementResDto> menuList, Long polledAt){
        return GetLunchListResDto.builder()
                .menus(menuList)
                .polledAt(polledAt)
                .totalPollCount(menuList.stream().mapToLong(GetLunchListElementResDto::getPollCount).sum())
                .build();
    }
}
