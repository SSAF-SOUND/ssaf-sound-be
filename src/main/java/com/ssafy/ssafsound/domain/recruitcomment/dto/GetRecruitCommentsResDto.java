package com.ssafy.ssafsound.domain.recruitcomment.dto;

import com.ssafy.ssafsound.domain.recruitcomment.domain.RecruitComment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Getter
@AllArgsConstructor
public class GetRecruitCommentsResDto {
    private Collection<RecruitCommentElement> recruitComments;

    public static GetRecruitCommentsResDto from(List<RecruitComment> comments) {
        Map<Long, RecruitCommentElement> treeMap = new TreeMap<>();
        for(RecruitComment comment: comments) {
            Long commentId = comment.getId();
            Long commentGroup = comment.getCommentGroup().getId();

            if(commentId.equals(commentGroup)) {
                treeMap.put(commentId, RecruitCommentElement.from(comment));
            } else {
                treeMap.get(commentGroup).addChild(RecruitCommentElement.from(comment));
            }
        }
        return new GetRecruitCommentsResDto(treeMap.values());
    }
}
