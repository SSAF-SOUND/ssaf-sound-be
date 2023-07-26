package com.ssafy.ssafsound.infra.storage.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ImagePathDto {

    private String imageDir;

    private String preSignedUrl;
}
