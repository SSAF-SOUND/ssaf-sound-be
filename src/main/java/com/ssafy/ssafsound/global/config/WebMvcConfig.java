package com.ssafy.ssafsound.global.config;

import com.ssafy.ssafsound.domain.auth.validator.AuthenticationArgumentResolver;
import com.ssafy.ssafsound.global.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.List;


@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;
    private final AuthenticationArgumentResolver authenticationArgumentResolver;

    public WebMvcConfig(AuthInterceptor authInterceptor, AuthenticationArgumentResolver authenticationArgumentResolver) {
        this.authInterceptor = authInterceptor;
        this.authenticationArgumentResolver = authenticationArgumentResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/auth/callback")
                .excludePathPatterns("/members/nickname")
                .excludePathPatterns("/boards");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticationArgumentResolver);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedOrigins("http://localhost:8081", "http://localhost:8082");
    }
}
