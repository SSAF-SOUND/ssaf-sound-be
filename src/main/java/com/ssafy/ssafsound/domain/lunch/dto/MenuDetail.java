package com.ssafy.ssafsound.domain.lunch.dto;

import com.ssafy.ssafsound.domain.lunch.domain.Lunch;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class MenuDetail {
    private String mainMenu;
    private String extraMenu;
    private String menuKcal;

    public static MenuDetail of(Lunch lunch){

        return MenuDetail.builder()
                .mainMenu(lunch.getMainMenu())
                .extraMenu(lunch.getExtraMenu())
                .menuKcal(lunch.getMenuKcal())
                .build();
    }
}
