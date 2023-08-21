package com.ssafy.ssafsound.domain.lunch.dto;

import com.ssafy.ssafsound.domain.lunch.domain.Lunch;
import lombok.Builder;
import lombok.Getter;
@Getter
@Builder
public class GetLunchListElementResDto {

    private Long lunchId;

    private String mainMenu;

    private String extraMenu;

    private String sumKcal;

    private String imagePath;

    private Long pollCount;

    public static GetLunchListElementResDto of(Lunch lunch, Long pollCount){
        return GetLunchListElementResDto.builder()
                .lunchId(lunch.getId())
                .mainMenu(lunch.getMainMenu())
                .extraMenu(lunch.getExtraMenu())
                .sumKcal(lunch.getSumKcal())
                .imagePath(lunch.getImagePath())
                .pollCount(pollCount)
                .build();
    }

}
