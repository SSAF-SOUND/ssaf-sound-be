package com.ssafy.ssafsound.domain.meta.validator;

import com.ssafy.ssafsound.domain.meta.domain.MetaDataType;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

@RequiredArgsConstructor
public class SkillsValidator implements ConstraintValidator<CheckSkills, List<String>> {

    private final MetaDataConsumer consumer;

    @Override
    public boolean isValid(List<String> values, ConstraintValidatorContext context) {
        values.forEach(skill->consumer.getMetaData(MetaDataType.SKILL.name(), skill));
        return true;
    }
}