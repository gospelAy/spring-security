package africa.semicolon.regcrow.security.filters;

import africa.semicolon.regcrow.dtos.request.CustomerRegistrationRequest;
import africa.semicolon.regcrow.utils.JwtUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import static africa.semicolon.regcrow.utils.AppUtils.*;
import static africa.semicolon.regcrow.utils.ExceptionUtils.AUTHENTICATION_FAILED_FOR_USER_WITH_EMAIL;
import static java.time.Instant.now;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RequiredArgsConstructor
public class RegcrowAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private ObjectMapper mapper = new ObjectMapper();




    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String email=EMPTY_SPACE_VALUE;
        try {
            CustomerRegistrationRequest bioData = mapper.readValue(request.getInputStream(), CustomerRegistrationRequest.class);
            email = bioData.getEmail();
            String password = bioData.getPassword();
            Authentication authDetails = new UsernamePasswordAuthenticationToken(email, password);
            Authentication authResult = authenticationManager.authenticate(authDetails);
            SecurityContextHolder.getContext().setAuthentication(authResult);
            return authResult;
        }catch (IOException exception){
            throw new BadCredentialsException(String.format(AUTHENTICATION_FAILED_FOR_USER_WITH_EMAIL, email));
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        String accessToken = generateAccessToken(authResult.getAuthorities());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.getOutputStream().write(mapper.writeValueAsBytes(
                Map.of(ACCESS_TOKEN_VALUE, accessToken)
        ));

    }


    private String generateAccessToken(Collection<? extends GrantedAuthority> authorities){
        return JWT.create()
                .withIssuedAt(now())
                .withExpiresAt(now().plusSeconds(1200L))
                .withClaim(CLAIMS_VALUE,authorities.stream()
                                                    .collect(Collectors
                                                            .toMap(grantedAuthority->CLAIM_VALUE,
                                                                    GrantedAuthority::getAuthority)))
                .sign(Algorithm.HMAC512(jwtUtil.getSecret().getBytes()));
    }
}
