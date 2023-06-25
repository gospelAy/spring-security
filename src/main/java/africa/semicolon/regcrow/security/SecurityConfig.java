package africa.semicolon.regcrow.security;


import africa.semicolon.regcrow.models.Role;
import africa.semicolon.regcrow.security.filters.RegcrowAuthenticationFilter;
import africa.semicolon.regcrow.security.manager.RegcrowAuthenticationManager;
import africa.semicolon.regcrow.security.user.User;
import africa.semicolon.regcrow.utils.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@AllArgsConstructor
public class SecurityConfig {

  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtUtil;






    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(c->c.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(c->c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterAt(new RegcrowAuthenticationFilter(authenticationManager, jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(c->c.requestMatchers(POST, "/api/v1/customer")
                        .permitAll())
                .authorizeHttpRequests(c->c.requestMatchers(POST, "/api/login")
                        .permitAll())
                .authorizeHttpRequests(c->c.requestMatchers(PATCH, "/api/v1/customer")
                        .hasRole(Role.CUSTOMER.name()))

                .authorizeHttpRequests(c->c.anyRequest().permitAll())
                .build();
    }
}
