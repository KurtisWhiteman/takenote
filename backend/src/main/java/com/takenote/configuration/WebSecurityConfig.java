package com.takenote.configuration;

import com.takenote.filter.JWTAuthorizationFilter;
import com.takenote.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.*;

import java.time.Duration;
import java.util.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // Make sure we can provide method level auth as well
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthService authService;

    private static final String API_LOGIN = "/api/v1/auth/login";
    private static final String FM_WEB_BASE_URL = (System.getenv("FM_WEB_BASE_URL") != null) ? System.getenv("FM_WEB_BASE_URL") : "http://localhost:8080";
    private static final String[] arrOfStr = FM_WEB_BASE_URL.split("//", 2);
    private static final String WWW_FM_WEB_BASE_URL = arrOfStr[0] + "//www." + arrOfStr[1];

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        var configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setAllowedOrigins(Arrays.asList(FM_WEB_BASE_URL, WWW_FM_WEB_BASE_URL));
        configuration.setAllowedMethods(List.of("OPTIONS", "GET", "HEAD", "POST", "PATCH", "PUT", "DELETE"));
        configuration.addExposedHeader("Authorization");
        configuration.setMaxAge(Duration.ofDays(1));

        http
                .cors()
                .and()
                .csrf().disable() // Disable cross site request forgery, as we don't use cookies - otherwise ALL PUT, POST, DELETE will get HTTP 403!
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(API_LOGIN).permitAll() // No auth required
                .anyRequest().authenticated() // Any request not specified above requires a user to be logged in
                .and()
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), authService));

    }

    @Configuration
    @EnableWebMvc
    public static class WebConfig implements WebMvcConfigurer {

        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedOrigins(FM_WEB_BASE_URL, WWW_FM_WEB_BASE_URL)
                    .allowedMethods("OPTIONS", "GET", "HEAD", "POST", "PATCH" ,"PUT", "DELETE")
                    .allowedHeaders("*")
                    .exposedHeaders("Authorization")
                    .allowCredentials(true).maxAge(3600);
        }
    }
}
