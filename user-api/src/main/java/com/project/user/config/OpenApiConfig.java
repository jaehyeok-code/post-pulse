package com.project.user.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    info = @Info(
        title = "PostPulse API",
        version = "1.0.0",
        description = "PostPulse 백엔드 REST API 문서입니다.\n" +
            "- 회원가입 · 인증 · 프로필 관리\n" +
            "- 게시글 · 댓글 · 좋아요 · 실시간 인기\n" +
            "- 검색 · 알림 서비스\n",
        contact = @Contact(
            name = "김재혁",
            email = "jaehyeok.ethan@gmail.com",
            url = "https://github.com/jaehyeok-code"
        )
    )
)
@Configuration
public class OpenApiConfig {
}
