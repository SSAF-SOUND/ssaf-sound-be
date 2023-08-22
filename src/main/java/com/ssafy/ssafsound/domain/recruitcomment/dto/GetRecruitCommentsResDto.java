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

    public static GetRecruitCommentsResDto of(List<RecruitComment> comments,
                                              Map<Long, Integer> likedCountMap,
                                              Map<Long, Boolean> memberLikedMap, Long memberId) {
        Map<Long, RecruitCommentElement> treeMap = new TreeMap<>();

        for(RecruitComment comment: comments) {
            Long commentId = comment.getId();
            Long commentGroup = comment.getCommentGroup().getId();
            Boolean mine = (comment.getMember().getId().equals(memberId));
            int likedCount = (likedCountMap.get(commentId) == null) ? 0 : likedCountMap.get(commentId);
            boolean liked = (memberLikedMap.get(commentId) != null);

            if(commentId.equals(commentGroup)) {
                treeMap.put(commentId, RecruitCommentElement.of(comment, likedCount, liked, mine));
            } else {
                treeMap.get(commentGroup).addChild(RecruitCommentElement.of(comment, likedCount, liked, mine));
            }
        }
        return new GetRecruitCommentsResDto(treeMap.values());
    }
}
