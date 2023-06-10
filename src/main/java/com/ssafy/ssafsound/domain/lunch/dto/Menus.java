package com.ssafy.ssafsound.domain.lunch.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class Menus {

    private Long totalVote;

    private List<Menu> menus;

    public Menus(List<Menu> menus){

    }
}
