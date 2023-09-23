package com.ssafy.ssafsound.domain.recruit.repository;

import com.ssafy.ssafsound.domain.recruit.domain.Recruit;
import com.ssafy.ssafsound.domain.recruit.dto.GetRecruitsReqDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface RecruitDynamicQueryRepository {
    Slice<Recruit> findRecruitByGetRecruitsReqDto(GetRecruitsReqDto dto, Pageable pageable);
    Slice<Recruit> findMemberJoinRecruitWithCursorAndPageable(Long memberId, String category, Long cursorId, Pageable pageable);
    Slice<Recruit> findMemberScrapRecruits(Long memberId, Long cursorId, Pageable pageable);
}
