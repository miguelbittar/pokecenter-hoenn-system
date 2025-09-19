package com.hoenn.pokecenter.config;

import com.hoenn.pokecenter.security.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/pokecenter/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/pokecenter/staff/nurse-joys").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/pokecenter/staff/nurse-joys").hasAnyRole("ADMIN", "COMMON")
                        .requestMatchers(HttpMethod.PUT, "/pokecenter/staff/nurse-joys/*/profile").hasAnyRole("ADMIN", "COMMON")
                        .requestMatchers(HttpMethod.PUT, "/pokecenter/staff/nurse-joys/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/pokecenter/staff/nurse-joys/*").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
