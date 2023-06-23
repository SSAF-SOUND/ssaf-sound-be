package com.ssafy.ssafsound.domain.member.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SemesterValidator implements ConstraintValidator<Semester, Integer> {

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if(value == null) return  true;
        Pattern pattern = Pattern.compile("^(?:[1-9]|10)$");
        Matcher matcher = pattern.matcher(String.valueOf(value));
        return matcher.matches();
    }
}
