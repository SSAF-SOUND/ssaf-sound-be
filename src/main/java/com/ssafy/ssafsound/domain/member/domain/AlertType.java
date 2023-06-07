package com.ssafy.ssafsound.domain.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name="alert_type")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertType {

    @Id
    @Column(name = "alert_type_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", length = 32)
    private String name;
}
