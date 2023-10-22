package com.ssafy.ssafsound.domain.post.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class GetPostOffsetReqDto extends BasePageRequest{
    private Long boardId;
}
