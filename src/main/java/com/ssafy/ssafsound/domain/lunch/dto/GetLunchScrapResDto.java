package com.ssafy.ssafsound.domain.lunch.dto;

import com.ssafy.ssafsound.domain.lunch.domain.Lunch;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Getter
@Builder
public class GetLunchScrapResDto {

    private String course;
    private String mainMenu;
    private String extraMenu;
    private String sumKcal;
    private String imagePath;

    private MetaData campus;

    private LocalDate createdAt;


    public static GetLunchScrapResDto fromWelstoryResponse(Map<String, String> info, MetaData campus) {
        return GetLunchScrapResDto.builder()
                .course(info.get("courseTxt"))
                .mainMenu(info.get("menuName"))
                .extraMenu(info.get("subMenu"))
                .sumKcal(info.get("sumKcal"))
                .imagePath(info.get("photoUrl") + info.get("photoCd"))
                .campus(campus)
                .createdAt(LocalDate.parse(info.get("menuDt"), DateTimeFormatter.ofPattern("yyyyMMdd")))
                .build();
    }

    public static GetLunchScrapResDto fromFreshmealResponse(Map<String, String> info, MetaData campus) {
        return GetLunchScrapResDto.builder()
                .course(info.get("corner"))
                .mainMenu(info.get("name"))
                .extraMenu(info.get("side"))
                .sumKcal(info.get("kcal"))
                .imagePath(info.get("thumbnailUrl"))
                .campus(campus)
                .createdAt(LocalDate.parse(info.get("menuDt"), DateTimeFormatter.ofPattern("yyyyMMdd")))
                .build();
    }

    public Lunch to() {
        return Lunch.builder()
                .course(this.course)
                .mainMenu(this.mainMenu)
                .extraMenu(this.extraMenu)
                .sumKcal(this.sumKcal)
                .imagePath(this.imagePath)
                .campus(this.campus)
                .createdAt(this.createdAt)
                .build();
    }
}
