package com.ssafy.ssafsound.domain.lunch.dto;

import com.ssafy.ssafsound.domain.lunch.domain.Lunch;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import lombok.Builder;
import lombok.Getter;
import org.json.JSONObject;

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


    public static GetLunchScrapResDto fromWelstoryResponse(JSONObject info, MetaData campus) {
        return GetLunchScrapResDto.builder()
                .course(info.getString("courseTxt"))
                .mainMenu(info.getString("menuName"))
                .extraMenu(info.getString("subMenu"))
                .sumKcal(info.getString("sumKcal"))
                .imagePath(info.getString("photoUrl") + info.getString("photoCd"))
                .campus(campus)
                .createdAt(LocalDate.parse(info.getString("menuDt"), DateTimeFormatter.ofPattern("yyyyMMdd")))
                .build();
    }

    public static GetLunchScrapResDto fromFreshmealResponse(JSONObject info, MetaData campus) {
        return GetLunchScrapResDto.builder()
                .course(info.getString("corner"))
                .mainMenu(info.getString("name"))
                .extraMenu(info.getString("side"))
                .sumKcal(info.getString("kcal"))
                .imagePath(info.getString("thumbnailUrl"))
                .campus(campus)
                .createdAt(LocalDate.parse(info.getString("menuDt"), DateTimeFormatter.ofPattern("yyyyMMdd")))
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
