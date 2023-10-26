package com.ssafy.ssafsound.domain.post.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class GetPostHotSearchOffsetReqDto extends BasePageRequest {
    @Size(min = 2)
    @NotBlank
    private String keyword;

    public void setKeyword(String keyword) {
        this.keyword = removeBlank(keyword);
    }

    private String removeBlank(String value) {
        return value.replaceAll(" ", "");
    }
}
