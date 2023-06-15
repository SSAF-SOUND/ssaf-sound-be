package com.ssafy.ssafsound.domain.meta.dto;

import com.ssafy.ssafsound.domain.meta.domain.MetaData;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetCampusesRepDto {

    private List<MetaData> campuses;
}
