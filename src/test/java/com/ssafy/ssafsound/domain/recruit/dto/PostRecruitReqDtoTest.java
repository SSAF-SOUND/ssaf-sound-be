package com.ssafy.ssafsound.domain.recruit.dto;

import com.ssafy.ssafsound.domain.meta.domain.MetaData;
import com.ssafy.ssafsound.domain.meta.domain.MetaDataType;
import com.ssafy.ssafsound.domain.meta.domain.RecruitType;
import com.ssafy.ssafsound.domain.meta.domain.Skill;
import com.ssafy.ssafsound.domain.meta.service.MetaDataConsumer;
import com.ssafy.ssafsound.domain.recruit.domain.Category;
import com.ssafy.ssafsound.domain.recruit.domain.Recruit;
import com.ssafy.ssafsound.domain.recruit.domain.RecruitQuestion;
import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PostRecruitReqDtoTest {

    @Mock
    MetaDataConsumer metaDataConsumer;

    @DisplayName("Recruit 등록 요청 엔티티 변환 Skill, Question Parameter Test")
    @MethodSource("invalidParameters")
    @ParameterizedTest(name = "{index}: {1}")
    void Given_SkillsAndNotQuestionsParameters_When_TryConvertEntity_Then_Success(String message, String recruitCategory, LocalDate recruitEndDate,
                                                                                      String title, String content, String registerRecruitType,
                                                                                      List<String> necessarySkills, List<String> freeQuestion) {

        PostRecruitReqDto dto = new PostRecruitReqDto(recruitCategory, recruitEndDate, title, content, registerRecruitType, necessarySkills, freeQuestion, null);

        Arrays.stream(Skill.values()).forEach((skill)->{
            Mockito.lenient().when(metaDataConsumer.getMetaData(MetaDataType.SKILL.name(), skill.getName())).thenReturn(new MetaData(skill));
        });

        Recruit convertResult = dto.to();
        List<String> questions = convertResult.getQuestions().stream()
                .map(RecruitQuestion::getContent).collect(Collectors.toList());

        assertAll(
                ()->assertEquals(Category.STUDY, convertResult.getCategory()),

                ()->{
                    if(freeQuestion == null) {
                        assertEquals(0, questions.size());
                    } else {
                        assertEquals(freeQuestion.size(), questions.size());
                        if (questions.size() != 0) {
                            for (int i = 0; i < questions.size(); ++i) {
                                assertEquals(freeQuestion.get(i), questions.get(i));
                            }
                        }
                    }
                }
        );
    }

    static Stream<Arguments> invalidParameters() {
        List<String> necessarySkills = Arrays.stream(Skill.values()).map(Skill::getName).collect(Collectors.toList());
        List<String> freeQuestion = Collections.singletonList("프로젝트/스터디 등록자가 참여자에게 묻고 싶은 자유 질문");
        return Stream.of(
                Arguments.of("엔티티 변환 Skill, Question Test", Category.STUDY.name(), LocalDate.now(),  "스터디/프로젝트 모집 제목", "컨텐츠", RecruitType.DESIGN.getName(), necessarySkills, freeQuestion),
                Arguments.of("엔티티 변환 Skill, Question Test", Category.STUDY.name(), LocalDate.now(),  "스터디/프로젝트 모집 제목", "컨텐츠", RecruitType.DESIGN.getName(), null, null)
        );
    }
}