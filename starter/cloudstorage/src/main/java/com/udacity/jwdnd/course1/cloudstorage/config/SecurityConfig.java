package com.udacity.jwdnd.course1.cloudstorage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import com.udacity.jwdnd.course1.cloudstorage.services.AuthenticationService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    private final AuthenticationService authenticationService;

    public SecurityConfig(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(httpForm ->{
                httpForm.loginPage("/login").permitAll();
                httpForm.defaultSuccessUrl("/home");
                
            })            
            .authorizeHttpRequests(registry ->{
                registry.requestMatchers("/signup","/css/**","/js/**").permitAll();
                registry.anyRequest().authenticated();
            })
            .logout(logout -> logout
                .logoutUrl("/logout") // Stellt sicher, dass /logout der POST-Endpunkt ist
                .logoutSuccessUrl("/login?logout") // Leitet nach erfolgreichem Logout um
                .permitAll()
            )
            .authenticationProvider(authenticationService)
            .build();
    }
}
