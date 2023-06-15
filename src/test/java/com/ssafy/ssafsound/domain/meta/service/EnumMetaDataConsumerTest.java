package com.ssafy.ssafsound.domain.meta.service;

import com.ssafy.ssafsound.domain.meta.fixture.TestMetaEnum;
import com.ssafy.ssafsound.global.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnumMetaDataConsumerTest {

    private MetaDataConsumer metaDataConsumer;

    @BeforeEach
    void InsertFixture() {
        metaDataConsumer = new EnumMetaDataConsumer();
        metaDataConsumer.putMetaData("TEST", TestMetaEnum.class);
    }

    @DisplayName("Enum 기반의 메타 데이터 등록 성공")
    @Test
    void Given_EnumMetaData_When_InsertMap_Then_Success() {
        metaDataConsumer = new EnumMetaDataConsumer();
        metaDataConsumer.putMetaData("TEST", TestMetaEnum.class);
        assertTrue(metaDataConsumer.getMetaDataList("TEST").size() != 0);
    }

    @DisplayName("존재하는 메타데이터 대분류의 리스트 조회 성공")
    @Test
    void Given_ExistMetaDataTypeString_When_FindMetaDataList_Then_Success() {
        assertTrue(metaDataConsumer.getMetaDataList("TEST").size() != 0);
    }

    @DisplayName("존재하지 않은 메타데이터 대분류의 리스트 조회 실패")
    @Test
    void Given_NotExistMetaDataTypeString_When_FindMetaDataList_Then_Fail() {
        assertThrows(ResourceNotFoundException.class, ()->{
            metaDataConsumer.getMetaDataList("NOT EXIST META DATA TYPE");
        });
    }

    @DisplayName("존재하는 메타데이터 단건 조회 성공")
    @Test
    void Given_MetaDataTypeStringAndExistMetaDataSubTypeString_When_FindMetaData_Then_Success() {
        metaDataConsumer.getMetaData("TEST", "메타데이터1");
    }

    @DisplayName("존재하지 않는 메타데이터 단건 조회 실패")
    @Test
    void Given_MetaDataTypeStringAndNotExistMetaDataSubTypeString_When_FindMetaData_Then_Fail() {
        assertThrows(ResourceNotFoundException.class, ()->{
            metaDataConsumer.getMetaData("TEST", "NOTEXISTDATA");
        });
    }
}