package com.ssafy.ssafsound.domain.lunch.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.ssafsound.domain.lunch.domain.Lunch;
import com.ssafy.ssafsound.domain.lunch.exception.LunchErrorInfo;
import com.ssafy.ssafsound.domain.lunch.exception.LunchException;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class GetFreshmealResDto implements GetScrapResDto{

    @JsonProperty("data")
    private FreshmealBodyData data;

    @Override
    public List<Lunch> getLunches(MetaData campus) {
        List<String> daysOfWeek = Arrays.asList("mo","tu","we","th","fr");
        List<Lunch> lunches = new ArrayList<>();

        for (String day : daysOfWeek) {
            this.data.getDailyMeal().get(day).getMeals().forEach(
                    meal -> {
                        try {
                            Lunch lunch = meal.toEntity(campus);
                            if (lunch != null) {
                                lunches.add(lunch);
                            }
                        } catch (Exception e) {
                            throw new LunchException(LunchErrorInfo.SCRAPING_ERROR);
                        }
                    }
            );
        }

        return lunches;
    }

    @Data
    @NoArgsConstructor
    public static class FreshmealBodyData {

        private Map<String, BoxedMeal> dailyMeal = new HashMap<>();

        // json 객체의 프로퍼티들을 Map에 맵핑하기 위한 setter 설정
        @JsonAnySetter
        public void put(String key, BoxedMeal value) {
            dailyMeal.put(key, value);
        }

        @Data
        @NoArgsConstructor
        public static class BoxedMeal {

            @JsonProperty("2")
            private List<Meal> meals = new ArrayList<>();

            @Data
            @NoArgsConstructor
            public static class Meal {
                private String corner;
                private String name;
                private String side;
                private String kcal;
                private String thumbnailUrl = null;
                private String mealDt;

                public Lunch toEntity(MetaData campus){
                    return Lunch.builder()
                            .course(this.corner)
                            .mainMenu(this.name)
                            .extraMenu(this.side)
                            .imagePath(this.thumbnailUrl)
                            .sumKcal(this.kcal)
                            .campus(campus)
                            .createdAt(LocalDate.parse(this.mealDt, DateTimeFormatter.ofPattern("yyyyMMdd")))
                            .build();
                }
            }
        }
    }

}
