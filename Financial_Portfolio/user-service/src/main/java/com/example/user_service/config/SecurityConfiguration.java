package com.example.user_service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static com.example.user_service.model.Permission.ADMIN_CREATE;
import static com.example.user_service.model.UserRole.ADMIN;
import static org.springframework.http.HttpMethod.POST;

import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JWTAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                                .requestMatchers("/addUser").hasRole(ADMIN.name())
                                .requestMatchers(POST, "/addUser").hasAuthority(ADMIN_CREATE.name())

//                                .requestMatchers("/helloWorld").hasRole(ADMIN.name())
//                                .requestMatchers(GET, "/helloWorld").hasAuthority(ADMIN_CREATE.name())

//                                .requestMatchers("/addUser").hasAuthority()
//                              .requestMatchers("/device/**").hasRole(UserRole.ADMIN.name())
//                              .requestMatchers("/device/find/**").hasAnyRole(UserRole.CLIENT.name(),UserRole.ADMIN.name())
//                              .requestMatchers("/login/**").permitAll()
//                              .requestMatchers("/loginUser").permitAll()
//                              .requestMatchers("/**").permitAll()
//                              .requestMatchers("/**").permitAll()
                                .requestMatchers("/register").permitAll()
                                .requestMatchers("/authenticate").permitAll()
//                              .requestMatchers("/securedHello").permitAll()
                                .anyRequest().authenticated()
                )
                .cors(customizer -> customizer.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*")); // Specify the allowed origins
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE")); // Allowed methods
        configuration.setAllowedHeaders(List.of("*")); // Allowed headers

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

