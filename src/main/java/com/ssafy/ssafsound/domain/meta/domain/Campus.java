package com.ssafy.ssafsound.domain.meta.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name="campus")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Campus {

    @Id
    @Column(name = "campus_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", length = 16)
    private String name;
}
