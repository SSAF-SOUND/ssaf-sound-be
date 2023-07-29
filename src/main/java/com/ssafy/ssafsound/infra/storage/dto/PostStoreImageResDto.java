package com.ssafy.ssafsound.infra.storage.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostStoreImageResDto {

    private String imagePath;

    private String imageUrl;

    private String preSignedUrl;
}
