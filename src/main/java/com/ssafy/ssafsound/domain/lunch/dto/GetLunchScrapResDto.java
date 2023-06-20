package com.ssafy.ssafsound.domain.lunch.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class GetLunchScrapResDto {

    private String course;
    private String mainMenu;
    private String extraMenu;
    private String sumKcal;
    private String imagePath;


    public static GetLunchScrapResDto fromWelstoryResponse(Map<String, String> info) {
        return GetLunchScrapResDto.builder()
                .course(info.get("courseTxt"))
                .mainMenu(info.get("menuName"))
                .extraMenu(info.get("subMenu"))
                .sumKcal(info.get("sumKcal"))
                .imagePath(info.get("photoUrl") + info.get("photoCd"))
                .build();
    }

    public static GetLunchScrapResDto fromFreshmealResponse(Map<String, String> info) {
        return GetLunchScrapResDto.builder()
                .course(info.get("corner"))
                .mainMenu(info.get("name"))
                .extraMenu(info.get("side"))
                .sumKcal(info.get("kcal"))
                .imagePath(info.get("thumbnailUrl"))
                .build();
    }
}
