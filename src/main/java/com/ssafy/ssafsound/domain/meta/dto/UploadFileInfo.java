package com.ssafy.ssafsound.domain.meta.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UploadFileInfo {
    private String filePath;
    private String fileUrl;
}
