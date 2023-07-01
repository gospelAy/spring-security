package africa.semicolon.regcrow.security;


import africa.semicolon.regcrow.security.filters.RegcrowAuthenticationFilter;
import africa.semicolon.regcrow.security.filters.RegcrowAuthorizationFilter;
import africa.semicolon.regcrow.utils.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static africa.semicolon.regcrow.models.Role.CUSTOMER;
import static africa.semicolon.regcrow.utils.AppUtils.*;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@AllArgsConstructor
public class SecurityConfig {

  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtUtil;



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      UsernamePasswordAuthenticationFilter authenticationFilter = new RegcrowAuthenticationFilter(authenticationManager, jwtUtil);
      authenticationFilter.setFilterProcessesUrl(LOGIN_ENDPOINT);
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(c->c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new RegcrowAuthorizationFilter(jwtUtil), RegcrowAuthenticationFilter.class)
                .addFilterAt(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(c->c.requestMatchers(POST, CUSTOMER_API_VALUE)
                        .permitAll())
                .authorizeHttpRequests(c->c.requestMatchers(POST, LOGIN_ENDPOINT)
                        .permitAll())
                .authorizeHttpRequests(c->c.requestMatchers(PATCH, UPDATE_CUSTOMER_ENDPOINT)
                        .hasRole(CUSTOMER.name()))
                .authorizeHttpRequests(c->c.anyRequest().authenticated())
                .build();
    }
}
