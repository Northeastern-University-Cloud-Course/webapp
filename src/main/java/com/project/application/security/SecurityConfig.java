package com.project.application.security;
import com.project.application.services.healthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.project.application.services.userDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Autowired
    private userDetailsService userDetailsService;

    @Autowired
    private healthService health;

    @Autowired
    private BCryptPasswordEncoder pwdEncoder;

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    public SecurityConfig() {
        log.info("SecurityConfig initialized");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(new AntPathRequestMatcher("/v1/user")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/healthz")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/error")).permitAll()

                        .anyRequest().authenticated())
                .csrf().disable()
                .httpBasic();

        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(pwdEncoder);

    }

}

