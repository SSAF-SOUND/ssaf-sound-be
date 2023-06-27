package com.ssafy.ssafsound.domain.post.dto;

import lombok.Getter;

@Getter
public class ImageUrl {
    private final String imageUrl;

    public ImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
