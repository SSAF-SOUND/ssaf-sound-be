package com.ssafy.ssafsound.domain.recruit.dto;

import lombok.Getter;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.data.domain.PageRequest;

@Getter
public class RecruitOffsetPagingRequestDto {

    protected Integer page;
    protected Integer size;

    @JsonIgnore
    public PageRequest getPageRequest() {
        int page = (this.page != null && this.page >= 0) ? this.page-1 : 0;
        int size = this.size == null ? 10 : this.size;
        return PageRequest.of(page, size);
    }
}
