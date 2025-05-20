package com.project.shopapp.configurations;

import com.project.shopapp.exceptions.usernameNotFound;
import com.project.shopapp.model.User;
import com.project.shopapp.respository.UserRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserRespository userRespository;

    @Bean
    public UserDetailsService userDetailsService() {
        return phoneNumber -> {
            User user = null;
            try {
                user = userRespository.findByPhoneNumber(phoneNumber)
                        .orElseThrow(() -> new usernameNotFound("Can't find user"));
            } catch (usernameNotFound e) {
                throw new RuntimeException(e);
            }
            return (org.springframework.security.core.userdetails.UserDetails) user;
        };
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();


    }
}
