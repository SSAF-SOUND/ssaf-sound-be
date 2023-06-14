package com.ssafy.ssafsound.domain.lunch.dto;

import com.ssafy.ssafsound.domain.lunch.domain.Lunch;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetLunchResDto {
    private String mainMenu;
    private String extraMenu;
    private String menuKcal;

    public static GetLunchResDto of(Lunch lunch){

        return GetLunchResDto.builder()
                .mainMenu(lunch.getMainMenu())
                .extraMenu(lunch.getExtraMenu())
                .menuKcal(lunch.getMenuKcal())
                .build();
    }
}
