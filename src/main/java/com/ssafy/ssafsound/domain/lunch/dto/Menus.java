package com.ssafy.ssafsound.domain.lunch.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class Menus {

    @Builder.Default
    private Long totalPollCount = 0L;

    @Builder.Default
    private List<Menu> menus = new ArrayList<>();

    public static Menus of(List<Menu> menuList){
        return Menus.builder()
                .menus(menuList)
                .totalPollCount(menuList.stream().mapToLong(Menu::getPollCount).sum())
                .build();
    }
}
