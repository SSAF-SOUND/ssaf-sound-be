package com.ssafy.ssafsound.global.validator;

import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ImageValidator implements ConstraintValidator<CheckImage, List<MultipartFile>> {

    @Override
    public boolean isValid(List<MultipartFile> files, ConstraintValidatorContext context) {
        if (files.get(0).isEmpty()) {
            return true;
        }

        List<String> imageExtensions = Arrays.asList("jpg", "jpeg", "png", "bmp", "webp");


        for (MultipartFile file : files) {
            // 확장자 검증
            String fileName = file.getOriginalFilename();
            String extension = Objects.requireNonNull(fileName).substring(fileName.lastIndexOf(".") + 1);
            if (!imageExtensions.contains(extension.toLowerCase()))
                return false;

            try {
                // 파일 타입 검증
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
