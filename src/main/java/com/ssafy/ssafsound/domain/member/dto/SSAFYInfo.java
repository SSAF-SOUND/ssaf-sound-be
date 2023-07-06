package com.ssafy.ssafsound.domain.member.dto;

import com.ssafy.ssafsound.domain.member.domain.Member;
import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import lombok.Getter;

@Getter
public class SSAFYInfo {

    private final Integer semester;
    private final String campus;
    private final String certificationState;
    private final String majorType;

    private SSAFYInfo(int semester, String campus, String certificationState) {
        this(semester, campus, certificationState, null);
    }

    private SSAFYInfo(int semester, String campus, String certificationState, String majorType) {
        this.semester = semester;
        this.campus = campus;
        this.certificationState = certificationState;
        this.majorType = majorType;
    }

    public static SSAFYInfo from(Member member) {
        return new SSAFYInfo(member.getSemester(), member.getCampus().getName(), member.getCertificationState().name(), member.getMajorType() == null ? null : member.getMajorType().getName());
    }
    public static SSAFYInfo of(int semester, String campus, String certificationState, MetaData majorType) {
        if (majorType == null) return new SSAFYInfo(semester, campus, certificationState);
        return new SSAFYInfo(semester, campus, certificationState, majorType.getName());
    }
}
