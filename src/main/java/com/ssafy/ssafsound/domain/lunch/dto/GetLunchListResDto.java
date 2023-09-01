package com.ssafy.ssafsound.domain.lunch.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Builder
public class GetLunchListResDto {

    @Builder.Default
    private Integer totalPollCount = 0;

    @Builder.Default
    private Integer polledAt = -1;

    @Builder.Default
    private List<GetLunchListElementResDto> menus = new ArrayList<>();

    public static GetLunchListResDto of(List<GetLunchListElementResDto> menuList, Integer polledAt){
        return GetLunchListResDto.builder()
                .menus(menuList)
                .polledAt(polledAt)
                .totalPollCount(menuList.stream().mapToInt(GetLunchListElementResDto::getPollCount).sum())
                .build();
    }

    public static GetLunchListResDto ofEmpty() {
        return GetLunchListResDto.builder()
                .menus(Collections.emptyList())
                .polledAt(-1)
                .totalPollCount(0)
                .build();
    }
}
