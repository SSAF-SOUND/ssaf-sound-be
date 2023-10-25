package com.ssafy.ssafsound.domain.recruit.event;

import com.ssafy.ssafsound.domain.event.MemberLeavedEvent;
import com.ssafy.ssafsound.domain.recruit.repository.RecruitLimitationRepository;
import com.ssafy.ssafsound.domain.recruit.repository.RecruitRepository;
import com.ssafy.ssafsound.domain.recruitapplication.repository.RecruitApplicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecruitEventListener {

    private final RecruitRepository recruitRepository;
    private final RecruitLimitationRepository recruitLimitationRepository
    private final RecruitApplicationRepository recruitApplicationrepository;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void transactionalEventListenerAfterCommit(MemberLeavedEvent memberLeavedEvent) {
        Long memberId = memberLeavedEvent.getMemberId();
        log.info("{} 회원 탈퇴로 인한 등록 리크루트글 일괄 삭제 및 리크루트 신청 일괄 취소", memberLeavedEvent.getMemberId());
        recruitRepository.deleteAllByMemberId(memberId);
        recruitLimitationRepository.decreaseCurrentNumberByMemberId(memberId);
        recruitApplicationrepository.cancelAllByMemberId(memberId);
    }
}
