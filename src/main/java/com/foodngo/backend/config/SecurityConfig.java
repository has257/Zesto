package com.foodngo.backend.config;

import com.foodngo.backend.entity.Donor;
import com.foodngo.backend.entity.Driver;
import com.foodngo.backend.entity.Ngos;
import com.foodngo.backend.repository.DonorRepository;
import com.foodngo.backend.repository.DriverRepository;
import com.foodngo.backend.repository.NgoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.nio.charset.StandardCharsets;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return bcrypt.encode(rawPassword);
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                if (isBcrypt(encodedPassword)) {
                    return bcrypt.matches(rawPassword, encodedPassword);
                }
                return java.security.MessageDigest.isEqual(
                        rawPassword.toString().getBytes(StandardCharsets.UTF_8),
                        encodedPassword.getBytes(StandardCharsets.UTF_8));
            }

            @Override
            public boolean upgradeEncoding(String encodedPassword) {
                return !isBcrypt(encodedPassword);
            }

            private boolean isBcrypt(String value) {
                return value != null && value.matches("^\\$2[aby]\\$.*");
            }
        };
    }

    @Bean
    public UserDetailsService userDetailsService(
            DonorRepository donorRepository,
            NgoRepository ngoRepository,
            DriverRepository driverRepository) {
        return email -> {
            Donor donor = donorRepository.findByEmail(email).orElse(null);
            if (donor != null) {
                return User.withUsername(email).password(donor.getPassword()).roles("DONOR").build();
            }
            Ngos ngo = ngoRepository.findByEmail(email).orElse(null);
            if (ngo != null) {
                return User.withUsername(email).password(ngo.getPassword()).roles("NGO").build();
            }
            Driver driver = driverRepository.findByEmail(email).orElse(null);
            if (driver != null) {
                return User.withUsername(email).password(driver.getPassword()).roles("DRIVER").build();
            }
            throw new UsernameNotFoundException("Account not found");
        };
    }

    @Bean
    public UserDetailsPasswordService userDetailsPasswordService(
            DonorRepository donorRepository,
            NgoRepository ngoRepository,
            DriverRepository driverRepository) {
        return (user, newPassword) -> {
            Donor donor = donorRepository.findByEmail(user.getUsername()).orElse(null);
            if (donor != null) {
                donor.setPassword(newPassword);
                donorRepository.save(donor);
                return User.withUserDetails(user).password(newPassword).build();
            }
            Ngos ngo = ngoRepository.findByEmail(user.getUsername()).orElse(null);
            if (ngo != null) {
                ngo.setPassword(newPassword);
                ngoRepository.save(ngo);
                return User.withUserDetails(user).password(newPassword).build();
            }
            Driver driver = driverRepository.findByEmail(user.getUsername()).orElse(null);
            if (driver != null) {
                driver.setPassword(newPassword);
                driverRepository.save(driver);
                return User.withUserDetails(user).password(newPassword).build();
            }
            throw new UsernameNotFoundException("Account not found");
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder,
            UserDetailsPasswordService userDetailsPasswordService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsPasswordService(userDetailsPasswordService);
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationProvider authenticationProvider) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/donors", "/api/ngos", "/api/drivers").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/statuses").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/drivers/**").hasAnyRole("DONOR", "NGO", "DRIVER")
                .requestMatchers("/api/drivers/**").hasRole("DRIVER")
                .requestMatchers("/api/donors/**").hasRole("DONOR")
                .requestMatchers("/api/ngos/**").hasRole("NGO")
                .requestMatchers(HttpMethod.GET, "/api/food-listings/**").authenticated()
                .requestMatchers("/api/food-listings/**").hasRole("DONOR")
                .requestMatchers(HttpMethod.GET, "/api/food-predictions/**").authenticated()
                .requestMatchers("/api/food-predictions/**").hasRole("DONOR")
                .requestMatchers("/api/donations/**").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/impact/**").authenticated()
                .requestMatchers("/api/impact/**").hasRole("NGO")
                .requestMatchers("/api/statuses/**").hasRole("DONOR")
                .anyRequest().authenticated())
            .httpBasic(Customizer.withDefaults())
            .authenticationProvider(authenticationProvider);
        return http.build();
    }
}