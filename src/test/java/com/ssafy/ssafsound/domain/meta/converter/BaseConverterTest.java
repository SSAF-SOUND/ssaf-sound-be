package com.ssafy.ssafsound.domain.meta.converter;

import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import com.ssafy.ssafsound.domain.meta.exception.MetaDataIntegrityException;
import com.ssafy.ssafsound.global.util.fixture.TestMetaEnum;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import com.ssafy.ssafsound.global.common.exception.GlobalErrorInfo;
import com.ssafy.ssafsound.global.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BaseConverterTest {

    @Mock
    private MetaDataConsumer metaDataConsumer;


    @DisplayName("MetaData객체 String으로 변환")
    @Test
    void Given_NotNullMetaData_When_ConvertString_Then_Success() {
        BaseConverter baseConverter = new BaseConverter(metaDataConsumer, "TEST");

        MetaData metaData = new MetaData(TestMetaEnum.TEST_META_FIXTURE1);

        assertEquals(baseConverter.convertToDatabaseColumn(metaData), TestMetaEnum.TEST_META_FIXTURE1.getName());
    }

    @DisplayName("String MetaData로 정상 변환")
    @Test
    void Given_ExistEnumTypeString_When_ConvertMetaData_Then_Success() {
        BaseConverter baseConverter = new BaseConverter(metaDataConsumer, "TEST");

        MetaData metaData = new MetaData(TestMetaEnum.TEST_META_FIXTURE1);
        String inputMetaDataType = TestMetaEnum.TEST_META_FIXTURE1.getName();

        Mockito.when(metaDataConsumer.getMetaData("TEST", inputMetaDataType)).thenReturn(metaData);

        assertEquals(baseConverter.convertToEntityAttribute(inputMetaDataType), metaData);
    }

    @DisplayName("database column null값이 주어진 경우 Null객체로 반환")
    @Test
    void Given_NullValue_When_ConvertMetaData_Then_success() {
        BaseConverter baseConverter = new BaseConverter(metaDataConsumer, "TEST");

        MetaData metaData = null;
        String value = null;

        assertAll(()->{
            assertEquals(baseConverter.convertToEntityAttribute(value), null);
        }, ()->{
            assertEquals(baseConverter.convertToDatabaseColumn(metaData), null);
        });
    }

    @DisplayName("무결성이 깨진 데이터베이스 column이 들어오는 경우 MetaData변환 실패")
    @Test
    void Given_NotExistEnumTypeString_When_ConvertMetaData_Then_Fail() {
        BaseConverter baseConverter = new BaseConverter(metaDataConsumer, "TEST");

        String inputMetaDataType = "NOT EXIST META DATA TYPE";

        Mockito.when(metaDataConsumer.getMetaData("TEST", inputMetaDataType)).thenThrow(new ResourceNotFoundException(GlobalErrorInfo.NOT_FOUND));

        assertThrows(MetaDataIntegrityException.class, ()->{
            baseConverter.convertToEntityAttribute(inputMetaDataType);
        });
    }

}