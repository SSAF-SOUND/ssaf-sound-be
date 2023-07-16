package com.ssafy.ssafsound.domain.post.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ImageUrlElement {
    private String imageUrl;

    public static ImageUrlElement from(String imageUrl) {
        return ImageUrlElement.builder()
                .imageUrl(imageUrl)
                .build();
    }
}
