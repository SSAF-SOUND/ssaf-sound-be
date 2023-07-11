package com.ssafy.ssafsound.global.util;

import org.junit.jupiter.params.converter.DefaultArgumentConverter;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;

public final class NullableConverter extends SimpleArgumentConverter {

    @Override
    public Object convert(Object source, Class<?> targetType) {
        if ("null".equals(source)) {
            return null;
        }

        return DefaultArgumentConverter.INSTANCE.convert(source, targetType);
    }
}
