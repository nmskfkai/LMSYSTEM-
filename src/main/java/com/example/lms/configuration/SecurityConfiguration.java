package com.example.lms.configuration;

import com.example.lms.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.reactive.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final MemberService memberService;


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/favicon.ico", "/image/**", "/css/**", "/js/**","/assets/**")
                ;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .cors(AbstractHttpConfigurer::disable)
//                .csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(request -> request
                .requestMatchers("/",
                        "/member/register",
                        "/member/email-auth",
                        "/member/find-password",
                        "/member/reset/password",
                        "/static/image/**",
                        "/auth/kakao/callback/**"
                ).permitAll()
                .requestMatchers("/admin/**")
                .hasAuthority("ROLE_ADMIN")
                .anyRequest().authenticated()); // 나머지 API에 대해서는 인증을 요구

//        http.authorizeHttpRequests(request -> request
//                .requestMatchers("/admin/**")
//                .hasAuthority("ROLE_ADMIN")
//        );

        //http.oauth2Login(Customizer.withDefaults());
        http
                .formLogin()
                .loginPage("/member/login")	// [A] 커스텀 로그인 페이지 지정
                .defaultSuccessUrl("/", true)
                //.usernameParameter("email")
                .permitAll();

        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                .logoutSuccessUrl("/member/login")
                .invalidateHttpSession(true);

        http.exceptionHandling(exception -> exception
                .authenticationEntryPoint(new UserAuthenticationFailureHandler())
                .accessDeniedPage("/error/denied")
        );
        http.csrf().disable();


        return http.build();
    }


    // 인증 관리자 관련 설정
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, MemberService memberService) throws Exception {
        return http
                .getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(memberService) //사용자 정보 서비스 설정
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }

}