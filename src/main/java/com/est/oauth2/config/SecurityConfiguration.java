package com.est.oauth2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .oauth2Login(Customizer.withDefaults())
                .authorizeHttpRequests(
                        auth -> auth.requestMatchers("/login")
                                    .anonymous()
                                .requestMatchers("/user/**")
                                    .hasAnyAuthority("USER")
                                .requestMatchers("/admin/**")
                                    .hasAnyAuthority("ADMIN")
                                .anyRequest()
                                    .authenticated()
                )
                .build();
    }

}
