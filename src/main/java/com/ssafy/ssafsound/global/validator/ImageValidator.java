package com.ssafy.ssafsound.global.validator;

import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;
import java.util.List;

public class ImageValidator implements ConstraintValidator<CheckImage, List<MultipartFile>> {

    @Override
    public boolean isValid(List<MultipartFile> files, ConstraintValidatorContext context) {
        if (files.get(0).isEmpty()) {
            return true;
        }

        for (MultipartFile file : files) {
            try {
                Tika tika = new Tika();
                String mimeType = tika.detect(file.getBytes());
                if (!mimeType.startsWith("image/"))
                    return false;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return true;
    }
}
