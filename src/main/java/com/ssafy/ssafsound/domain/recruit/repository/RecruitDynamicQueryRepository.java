package com.ssafy.ssafsound.domain.recruit.repository;

import com.ssafy.ssafsound.domain.recruit.domain.Recruit;
import com.ssafy.ssafsound.domain.recruit.dto.AppliedRecruit;
import com.ssafy.ssafsound.domain.recruit.dto.GetRecruitsCursorReqDto;
import com.ssafy.ssafsound.domain.recruit.dto.GetRecruitsOffsetReqDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface RecruitDynamicQueryRepository {
    Slice<Recruit> findRecruitSliceByGetRecruitsReqDto(GetRecruitsCursorReqDto dto, Pageable pageable);
    Page<Recruit> findRecruitPageByGetRecruitsReqDto(GetRecruitsOffsetReqDto dto, Pageable pageable);
    Slice<Recruit> findMemberJoinRecruitWithCursorAndPageable(Long memberId, String category, Long cursorId, Pageable pageable);
    Page<Recruit> findMemberJoinRecruitWithPageable(Long memberId, String category, Pageable pageable);
    Slice<Recruit> findMemberScrapRecruitsByCursor(Long memberId, Long cursorId, Pageable pageable);
    Page<Recruit> findMemberScrapRecruitsByPage(Long memberId, Pageable pageable);
    Slice<AppliedRecruit> findMemberAppliedRecruitsByCursor(Long memberId, Long cursor, String category, String matchStatus, Pageable pageable);

    Page<AppliedRecruit> findMemberAppliedRecruitsByPage(Long memberId, String category, String matchStatus, Pageable pageable);
}
