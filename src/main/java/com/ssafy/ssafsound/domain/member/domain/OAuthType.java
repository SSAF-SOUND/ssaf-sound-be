package com.ssafy.ssafsound.domain.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name="oauth_type")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuthType {

    @Id
    @Column(name = "oauth_type_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", length = 16)
    private String name;
}
