package com.ssafy.ssafsound.domain.lunch.domain;

import com.ssafy.ssafsound.domain.meta.converter.CampusConverter;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private String course;

    @Column
    private String mainMenu;

    @Column(length = 1023)
    private String extraMenu;

    @Column
    private String imagePath;

    @Column
    private String sumKcal;

    @Convert(converter = CampusConverter.class)
    private MetaData campus;

    @Column
    private LocalDate createdAt;

    @OneToMany(mappedBy = "lunch")
    @Builder.Default
    private List<LunchPoll> lunchPolls = new ArrayList<>();

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != getClass()) return false;

        Lunch lunch = (Lunch) obj;
        return this.getId() == lunch.getId();
    }

    @Override
    public int hashCode(){
        return Objects.hash(getId());
    }
}
