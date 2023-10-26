package com.ssafy.ssafsound.domain.recruit.dto;

import java.util.List;

public interface RecruitPaging<T> {
    T getNextPaging();
    String getCategory();
    String getKeyword();
    boolean isFinished();

    List<String> getRecruitTypes();
    List<String> getSkills();
}
