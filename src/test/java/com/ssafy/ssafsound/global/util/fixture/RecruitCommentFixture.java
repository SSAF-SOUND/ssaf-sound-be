package com.ssafy.ssafsound.global.util.fixture;

import com.ssafy.ssafsound.domain.BaseTimeEntity;
import com.ssafy.ssafsound.domain.recruitcomment.domain.RecruitComment;
import com.ssafy.ssafsound.domain.recruitcomment.dto.GetRecruitCommentsResDto;
import com.ssafy.ssafsound.domain.recruitcomment.dto.PatchRecruitCommentReqDto;
import com.ssafy.ssafsound.domain.recruitcomment.dto.PostRecruitCommentLikeResDto;
import com.ssafy.ssafsound.domain.recruitcomment.dto.PostRecruitCommentReqDto;
import com.ssafy.ssafsound.domain.recruitcomment.dto.PostRecruitCommentResDto;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecruitCommentFixture {

    private static final Long DEFAULT_COMMENT_GROUP = -1L;

    private static final MemberFixture memberFixture = new MemberFixture();

    public static final PostRecruitCommentReqDto POST_RECRUIT_COMMENT_REQ_DTO = new PostRecruitCommentReqDto("공모전 수상이 목표인가요?", DEFAULT_COMMENT_GROUP);

    public static final PostRecruitCommentResDto POST_RECRUIT_COMMENT_RES_DTO
            = new PostRecruitCommentResDto(1L, "공모전 수상이 목표인가요?",
            memberFixture.createMember().getId(), memberFixture.createMember().getNickname(), 1L);

    public static final PatchRecruitCommentReqDto PATCH_RECRUIT_COMMENT_REQ_DTO = new PatchRecruitCommentReqDto("덧글 수정");

    public static final RecruitComment RECRUIT_COMMENT_1 = RecruitComment.builder()
            .id(1L)
            .content("공모전 수상이 목표인가요?")
            .deletedComment(false)
            .recruit(RecruitFixture.RECRUIT_1)
            .member(memberFixture.createMember())
            .build();

    public static final PostRecruitCommentLikeResDto POST_RECRUIT_COMMENT_LIKE_RES_DTO = new PostRecruitCommentLikeResDto(1, true);

    private static Map<Long, Integer> likedCountMap = new HashMap<>();

    private static Map<Long, Boolean> memberLikedMap = new HashMap<>();

    static {
        ReflectionTestUtils.setField(RECRUIT_COMMENT_1, BaseTimeEntity.class,"createdAt", LocalDateTime.now(), LocalDateTime.class);
        RECRUIT_COMMENT_1.setCommentGroup(RECRUIT_COMMENT_1);
        likedCountMap.put(1L, 0);
    }

    public static GetRecruitCommentsResDto GET_RECRUIT_RES_DTO = GetRecruitCommentsResDto.of(List.of(RECRUIT_COMMENT_1), likedCountMap, memberLikedMap, 2L);
}
