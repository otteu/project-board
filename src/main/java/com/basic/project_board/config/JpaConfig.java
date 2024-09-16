package com.basic.project_board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class JpaConfig {


    // @CreatedBy 옵션 설정시 값이 넣어지지 안는 부분 초기 설정 추후 값 넣게 변경
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of("user01"); // TODO : 스프링 시쿠리티로 인증 기능 수정
    }

}
