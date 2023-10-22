package com.ssafy.ssafsound.domain.post.dto;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class GetPostMyScrapOffsetReqDto extends BasePageRequest {
    @Override
    public PageRequest toPageRequest() {
        return PageRequest.of(super.getPage() - 1, super.getSize(), Sort.Direction.ASC, "id");
    }
}
