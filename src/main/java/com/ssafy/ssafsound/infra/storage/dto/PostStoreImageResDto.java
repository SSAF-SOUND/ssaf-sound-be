package com.ssafy.ssafsound.infra.storage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PostStoreImageResDto {

    private List<ImagePathDto> imagePathDtos;
}
