package site.treetory.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import site.treetory.global.exception.CustomAuthenticationEntryPoint;
import site.treetory.global.security.jwt.JwtAuthenticationFilter;
import site.treetory.global.security.jwt.JwtUtils;
import site.treetory.global.security.oauth.CustomOAuth2UserService;
import site.treetory.global.security.oauth.Oauth2SuccessHandler;
import site.treetory.global.util.CookieUtils;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtils jwtUtils;
    private final CookieUtils cookieUtils;
    private final UserDetailsService userDetailsService;
    private final CustomOAuth2UserService oAuth2UserService;
    private final Oauth2SuccessHandler oauth2SuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(GET, "/api/trees/*").permitAll()
                        .requestMatchers(POST, "/api/trees/*/ornaments").permitAll()
                        .requestMatchers(GET, "/api/ornaments").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .requestMatchers(GET, "/actuator/health").permitAll()
                        .anyRequest().denyAll()
                )
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
                )
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(STATELESS)
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtils, cookieUtils, userDetailsService),
                        UsernamePasswordAuthenticationFilter.class
                )
                .oauth2Login(oauth2Configurer -> oauth2Configurer
                        .authorizationEndpoint(authEndPoint -> authEndPoint
                                .baseUri("/api/auth/login"))
                        .redirectionEndpoint(authEndPoint -> authEndPoint
                                .baseUri("/api/auth/oauth2/*"))
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService))
                        .successHandler(oauth2SuccessHandler)
                );

        return http.build();
    }
}
