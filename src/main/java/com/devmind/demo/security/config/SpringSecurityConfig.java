package com.devmind.demo.security.config;

import com.devmind.demo.security.dao.UserDao;
import com.devmind.demo.security.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SpringSecurityConfig {

    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserDao userDao) {
        // We create a password encoder
        PasswordEncoder encoder = passwordEncoder();
        return username -> {
            User user = userDao.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException(username);
            }
            return user;
        };
    }

    @Bean
    @Order(1)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/console/**")).hasAnyRole(ROLE_ADMIN)
                        .anyRequest().authenticated()
                )
                .formLogin(
                        form -> form
                                .loginPage("/login.html")
                                .permitAll()
                                .loginProcessingUrl("/login")
                                // On success we load the main page
                                .defaultSuccessUrl("/index.html", true)
                                // On failure we redirect to /login.html?error=true and we display an error message
                                .failureHandler((request, response, exception) -> {
                                    exception.printStackTrace();
                                    response.sendRedirect("/login.html?error=true");
                                })
                )
                .httpBasic(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }
}
