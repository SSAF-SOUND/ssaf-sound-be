package com.ssafy.ssafsound.domain.meta.validator;

import com.ssafy.ssafsound.domain.meta.domain.MetaDataType;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import com.ssafy.ssafsound.global.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class RecruitTypeValidator implements ConstraintValidator<CheckRecruitType, String> {

    private final MetaDataConsumer consumer;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            consumer.getMetaData(MetaDataType.RECRUIT_TYPE.name(), value);
        } catch (ResourceNotFoundException e) {
            return false;
        }
        return true;
    }
}