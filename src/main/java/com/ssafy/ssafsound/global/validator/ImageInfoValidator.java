package com.ssafy.ssafsound.global.validator;

import com.ssafy.ssafsound.domain.post.dto.ImageInfo;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class ImageInfoValidator implements ConstraintValidator<CheckImageInfo, List<ImageInfo>> {

    @Override
    public boolean isValid(List<ImageInfo> images, ConstraintValidatorContext context) {
        if (images == null) {
            return false;
        }

        if (images.size() == 0)
            return true;

        for (ImageInfo image : images) {
            if (image.getImagePath() == null || image.getImageUrl() == null)
                return false;
        }

        return true;
    }
}
