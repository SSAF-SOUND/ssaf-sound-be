package com.ssafy.ssafsound.domain.recruit.repository;

import com.ssafy.ssafsound.domain.recruit.domain.Recruit;
import com.ssafy.ssafsound.domain.recruit.dto.AppliedRecruit;
import com.ssafy.ssafsound.domain.recruit.dto.GetRecruitsReqDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface RecruitDynamicQueryRepository {
    Slice<Recruit> findRecruitSliceByGetRecruitsReqDto(GetRecruitsReqDto dto, Pageable pageable);
    Page<Recruit> findRecruitPageByGetRecruitsReqDto(GetRecruitsReqDto dto, Pageable pageable);
    Slice<Recruit> findMemberJoinRecruitWithCursorAndPageable(Long memberId, String category, Long cursorId, Pageable pageable);
    Slice<Recruit> findMemberScrapRecruits(Long memberId, Long cursorId, Pageable pageable);
    Slice<AppliedRecruit> findMemberAppliedRecruits(Long memberId, Long cursor, String category, String matchStatus, Pageable pageable);
}
