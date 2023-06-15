package com.ssafy.ssafsound.domain.lunch.domain;

import com.ssafy.ssafsound.domain.BaseTimeEntity;
import com.ssafy.ssafsound.domain.meta.converter.CampusConverter;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity(name="lunch")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lunch {

    @Id
    @Column(name = "lunch_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String mainMenu;

    @Column
    private String extraMenu;

    @Column
    private String imagePath;

    @Column
    private String menuKcal;

    @Convert(converter = CampusConverter.class)
    private MetaData campus;

    @Column
    private LocalDate createdAt;

    @OneToMany(mappedBy = "lunch")
    @Builder.Default
    private List<LunchPoll> lunchPolls = new ArrayList<>();
}
