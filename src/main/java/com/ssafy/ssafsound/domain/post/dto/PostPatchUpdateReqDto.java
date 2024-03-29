package com.ssafy.ssafsound.domain.post.dto;

import com.ssafy.ssafsound.global.validator.CheckImageInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@Builder
public class PostPatchUpdateReqDto {
    @NotBlank
    @Size(min = 2, max = 100)
    private String title;

    @NotBlank
    @Size(min = 2)
    private String content;

    private boolean anonymity;

    @CheckImageInfo
    private List<ImageInfo> images;
}
