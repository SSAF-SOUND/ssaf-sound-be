package com.ssafy.ssafsound.domain.member.validator;

import org.springframework.beans.factory.annotation.Value;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SemesterValidator implements ConstraintValidator<Semester, Integer> {

    @Value("${spring.constant.semester.MIN_SEMESTER}")
    private Integer MIN_SEMESTER;
    @Value("${spring.constant.semester.MAX_SEMESTER}")
    private Integer MAX_SEMESTER;
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if(value == null) return  true;
        return value >= MIN_SEMESTER && value <= MAX_SEMESTER;
    }
}
