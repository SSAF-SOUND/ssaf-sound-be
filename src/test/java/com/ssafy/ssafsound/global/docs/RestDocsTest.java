package com.ssafy.ssafsound.global.docs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.ssafsound.domain.auth.service.token.JwtTokenProvider;
import com.ssafy.ssafsound.domain.auth.validator.AuthenticationArgumentResolver;
import com.ssafy.ssafsound.global.config.JpaConfig;
import com.ssafy.ssafsound.global.config.WebMvcConfig;
import com.ssafy.ssafsound.global.interceptor.AuthInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@Import(RestDocsConfiguration.class)
@AutoConfigureRestDocs
@WebMvcTest(excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebMvcConfig.class),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JpaConfig.class)
})
public class RestDocsTest {
    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected RestDocumentationResultHandler restDocs;

    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @MockBean
    protected AuthenticationArgumentResolver authenticationArgumentResolver;

    @MockBean
    protected AuthInterceptor authInterceptor;

    protected MockMvc mockMvc;


    protected String toJson(Object value) throws JsonProcessingException {
        return objectMapper.writeValueAsString(value);
    }

    @BeforeEach
    public void setMockMvc(WebApplicationContext context, RestDocumentationContextProvider provider) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(
                        documentationConfiguration(provider)
                                .uris()
                                .withScheme("http")
                                .withHost("127.0.0.1")
                                .withPort(8080))
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .alwaysDo(restDocs)
                .build();

        doReturn(true)
                .when(authInterceptor)
                .preHandle(any(), any(), any());
    }
}
