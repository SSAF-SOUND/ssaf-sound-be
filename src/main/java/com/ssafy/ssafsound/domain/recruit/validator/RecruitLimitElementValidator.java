package com.ssafy.ssafsound.domain.recruit.validator;

import com.ssafy.ssafsound.domain.meta.domain.MetaDataType;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import com.ssafy.ssafsound.domain.recruit.dto.RecruitLimitElement;
import com.ssafy.ssafsound.global.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

@RequiredArgsConstructor
public class RecruitLimitElementValidator implements ConstraintValidator<CheckRecruitLimitElement, List<RecruitLimitElement>> {

    private final MetaDataConsumer consumer;

    @Override
    public boolean isValid(List<RecruitLimitElement> value, ConstraintValidatorContext context) {
        try {
            value.forEach(element -> consumer.getMetaData(MetaDataType.RECRUIT_TYPE.name(), element.getRecruitType()));
        } catch (ResourceNotFoundException e) {
            return false;
        }
        return true;
    }
}