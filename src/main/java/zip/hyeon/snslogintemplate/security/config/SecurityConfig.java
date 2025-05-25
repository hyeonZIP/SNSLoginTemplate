package zip.hyeon.snslogintemplate.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import zip.hyeon.snslogintemplate.security.oauth2.handler.OAuth2SuccessHandler;
import zip.hyeon.snslogintemplate.security.oauth2.service.CustomOAuth2UserService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() { // security 를 적용하지 않을 리소스
        return web -> web.ignoring()
                .requestMatchers("/error", "/favicon.ico");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // csrf 비활성화, 쿠키 사용시, 활성화 필요
                .formLogin(AbstractHttpConfigurer::disable) // 기본 로그인 비활성화
                .sessionManagement(AbstractHttpConfigurer::disable) //
                .logout(AbstractHttpConfigurer::disable) // 기본 로그아웃 비활성화
                .httpBasic(AbstractHttpConfigurer::disable) // 기본 인증 로그인 비활성화
                .headers(c -> c.frameOptions(
                        FrameOptionsConfig::disable).disable()) // X-Frame-Options 비활성화
                .sessionManagement(c ->
                        c.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용하지 않음
        ;

        http
                .oauth2Login(oauth2 -> oauth2
                        // 로그인
                        .loginPage("/login")

                        // SNS 로그인 화면 요청 URI / default : "/oauth2/authorization/{로그인할 SNS 타입 ex) kakao}"
                        .authorizationEndpoint(authorization -> authorization
                                .baseUri("/login"))

                        // 인가 코드 리다이렉션 URI / default : "/oauth2/authorization/{registrationId}"
                        .redirectionEndpoint(redirection -> redirection
                                .baseUri("/login/code/{registrationId}"))

                        // OAuth2 로그인 성공 시 반환 정보 처리 로직
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService))

                        .successHandler(oAuth2SuccessHandler)

                );

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login/**").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
