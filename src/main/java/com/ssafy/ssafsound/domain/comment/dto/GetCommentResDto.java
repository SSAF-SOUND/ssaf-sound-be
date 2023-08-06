package com.ssafy.ssafsound.domain.comment.dto;

import com.ssafy.ssafsound.domain.auth.dto.AuthenticatedMember;
import com.ssafy.ssafsound.domain.comment.domain.Comment;
import lombok.Builder;
import lombok.Getter;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class GetCommentResDto {
    private Collection<GetCommentElement> comments;

    public static GetCommentResDto of(List<Comment> comments, AuthenticatedMember loginMember) {
        Map<Long, GetCommentElement> linkedHashMap = new LinkedHashMap<>();

        for (Comment comment : comments) {
            Long commentId = comment.getId();
            Long commentGroup = comment.getCommentGroup().getId();

            if (commentId.equals(commentGroup)) {
                linkedHashMap.put(commentId, GetCommentElement.of(comment, loginMember));
            } else {
                linkedHashMap.get(commentGroup).addReply(GetCommentElement.of(comment, loginMember));
            }
        }
        return GetCommentResDto.builder()
                .comments(linkedHashMap.values())
                .build();
    }
}
