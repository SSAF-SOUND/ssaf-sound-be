package com.ssafy.ssafsound.domain.meta.dto;

import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetRecruitTypeResDto {
    private List<MetaData> recruitTypes;
}
