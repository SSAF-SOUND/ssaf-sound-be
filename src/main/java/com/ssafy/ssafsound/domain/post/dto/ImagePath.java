package com.ssafy.ssafsound.domain.post.dto;

import lombok.Getter;

@Getter
public class ImagePath {
    private final String imagePath;

    public ImagePath(String image) {
        this.imagePath = image;
    }
}
