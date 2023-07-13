package com.ssafy.ssafsound.domain.comment.dto;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.comment.domain.Comment;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class GetCommentResDto {
    List<GetCommentElement> comments;

    public static GetCommentResDto of(List<Comment> comments, AuthenticatedMember member) {
        return GetCommentResDto.builder()
                .comments(comments.stream()
                        .map(comment -> GetCommentElement.of(comment, member))
                        .collect(Collectors.toList()))
                .build();
    }
}
