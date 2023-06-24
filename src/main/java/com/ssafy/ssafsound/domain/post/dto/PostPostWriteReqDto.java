package com.ssafy.ssafsound.domain.post.dto;

import com.ssafy.ssafsound.domain.post.validator.CheckFileCount;
import com.ssafy.ssafsound.domain.post.validator.CheckFileSize;
import com.ssafy.ssafsound.domain.post.validator.CheckImage;
import com.ssafy.ssafsound.domain.post.validator.CheckImageExtension;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PostPostWriteReqDto {
    @NotBlank
    @Size(min = 2, max = 100)
    private String title;

    @NotBlank
    @Size(min = 2)
    private String content;

    private boolean anonymous;

    @CheckFileCount(maxFileCount = 1)
    @CheckFileSize(maxFileSize = 50 * 1024)
    @CheckImageExtension
    @CheckImage
    private List<MultipartFile> images;
}
