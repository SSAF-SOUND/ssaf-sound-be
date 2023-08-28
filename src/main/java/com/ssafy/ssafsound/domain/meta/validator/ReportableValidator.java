package com.ssafy.ssafsound.domain.meta.validator;

import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import com.ssafy.ssafsound.domain.meta.domain.MetaDataType;
import com.ssafy.ssafsound.domain.meta.domain.SourceType;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

@RequiredArgsConstructor
public class ReportableValidator implements ConstraintValidator<CheckReportable, String> {

    private final MetaDataConsumer consumer;

    private final List<SourceType> reportableSourceTypes = List.of(SourceType.POST, SourceType.COMMENT, SourceType.CHAT,
            SourceType.RECRUIT, SourceType.RECRUIT_COMMENT);

    @Override
    public boolean isValid(String source, ConstraintValidatorContext context) {

        MetaData sourceType = consumer.getMetaData(MetaDataType.SOURCE_TYPE.name(), source);


        return reportableSourceTypes.stream().anyMatch(type -> type.getName().equals(sourceType.getName()));

    }
}