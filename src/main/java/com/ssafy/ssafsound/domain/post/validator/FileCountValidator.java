package com.ssafy.ssafsound.domain.post.validator;

import com.ssafy.ssafsound.domain.post.exception.PostErrorInfo;
import com.ssafy.ssafsound.domain.post.exception.PostException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

@Slf4j
public class FileCountValidator implements ConstraintValidator<CheckFileCount, List<MultipartFile>> {
    private int maxFileCount;

    @Override
    public void initialize(CheckFileCount constraintAnnotation) {
        this.maxFileCount = constraintAnnotation.maxFileCount();
    }

    @Override
    public boolean isValid(List<MultipartFile> files, ConstraintValidatorContext context) {
        if (files.get(0).isEmpty()) {
            return true;
        }
        return files.size() <= maxFileCount;
    }
}
