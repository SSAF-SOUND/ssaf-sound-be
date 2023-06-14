package com.ssafy.ssafsound.domain.lunch.dto;

import com.ssafy.ssafsound.domain.lunch.domain.Lunch;
import lombok.Builder;
import lombok.Getter;
@Getter
@Builder
public class Menu {

    private String mainMenu;

    private String imagePath;

    private Long pollCount;

    public static Menu of(Lunch lunch, Long pollCount){
        return Menu.builder()
                .mainMenu(lunch.getMainMenu())
                .imagePath(lunch.getImagePath())
                .pollCount(pollCount)
                .build();
    }

}
