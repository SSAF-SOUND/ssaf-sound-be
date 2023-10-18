package com.ssafy.ssafsound.domain.post.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ImageInfo {
    private String imagePath;
    private String imageUrl;
}
