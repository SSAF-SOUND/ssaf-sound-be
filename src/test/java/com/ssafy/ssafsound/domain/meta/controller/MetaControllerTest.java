package com.ssafy.ssafsound.domain.meta.controller;

import com.ssafy.ssafsound.domain.meta.domain.MetaDataType;
import com.ssafy.ssafsound.global.util.fixture.ProductionMetaDataFixture;
import com.ssafy.ssafsound.global.docs.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MetaControllerTest extends ControllerTest {

    @DisplayName("캠퍼스 목록 조회에 성공합니다.")
    @Test
    void getCampuses() {

        given(enumMetaDataConsumer.getMetaDataList(MetaDataType.CAMPUS.name()))
                .willReturn(ProductionMetaDataFixture.CAMPUSES);

        restDocs
                .when().get("/meta/campuses")
                .then().log().all()
                .assertThat()
                .apply(document("meta/campuses",
                    getEnvelopPatternWithData()
                        .andWithPrefix("data.campuses[].",
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("캠퍼스 id (미사용)"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("캠퍼스 이름")
                        )
                    )
                ).expect(status().isOk());
    }

    @DisplayName("스킬 목록 조회에 성공합니다.")
    @Test
    void getSkills() {

        given(enumMetaDataConsumer.getMetaDataList(MetaDataType.SKILL.name()))
                .willReturn(ProductionMetaDataFixture.SKILLS);

        restDocs
                .when().get("/meta/skills")
                .then().log().all()
                .assertThat()
                .apply(document("meta/skills",
                        getEnvelopPatternWithData()
                                .andWithPrefix("data.skills[].",
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("스킬 id (미사용)"),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("스킬 이름")
                                )
                        )
                ).expect(status().isOk());
    }

    @DisplayName("리크루트 모집타입 목록 조회에 성공합니다.")
    @Test
    void getRecruitTypes() {

        given(enumMetaDataConsumer.getMetaDataList(MetaDataType.RECRUIT_TYPE.name()))
                .willReturn(ProductionMetaDataFixture.RECRUIT_TYPES);

        restDocs
                .when().get("/meta/recruit-types")
                .then().log().all()
                .assertThat()
                .apply(document("meta/recruit-types",
                        getEnvelopPatternWithData()
                                .andWithPrefix("data.recruitTypes[].",
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("리크루트 모집타입  id (미사용)"),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("리크루트 모집타입 이름")
                                )
                        )
                ).expect(status().isOk());
    }
}
