package com.ssafy.ssafsound.domain.recruitcomment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostRecruitCommentLikeResDto {
	private Integer likeCount;
	private Boolean liked;
}
