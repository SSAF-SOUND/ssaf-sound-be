package com.ssafy.ssafsound.domain.lunch.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.ssafsound.domain.lunch.domain.Lunch;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@NoArgsConstructor
public class GetWelstoryResDto implements GetScrapResDto{
    @JsonProperty("data")
    private WelstoryBodyData welstoryBodyData;

    @Data
    @NoArgsConstructor
    public static class WelstoryBodyData{

        List<Meal> mealList;

        @Data
        @NoArgsConstructor
        public static class Meal{
            private String courseTxt;
            private String menuName;
            private String subMenuTxt;
            private String sumKcal;
            private String photoUrl;
            private String photoCd;
            private String menuDt;

            public Lunch toEntity(MetaData campus){
                return Lunch.builder()
                        .course(this.courseTxt)
                        .mainMenu(this.menuName)
                        .extraMenu(this.subMenuTxt)
                        .imagePath( this.photoCd != null ? this.photoUrl + this.photoCd : null)
                        .sumKcal(this.sumKcal)
                        .campus(campus)
                        .createdAt(LocalDate.parse(this.menuDt, DateTimeFormatter.ofPattern("yyyyMMdd")))
                        .build();
            }
        }
    }
}
