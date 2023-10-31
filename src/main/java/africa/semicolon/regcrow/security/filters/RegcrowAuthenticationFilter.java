package africa.semicolon.regcrow.security.filters;

import africa.semicolon.regcrow.dtos.request.LoginRequest;
import africa.semicolon.regcrow.utils.JwtUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.HashMap;
import java.util.Map;

import static africa.semicolon.regcrow.utils.AppUtils.*;
import static africa.semicolon.regcrow.utils.ExceptionUtils.AUTHENTICATION_FAILED_FOR_USER_WITH_EMAIL;
import static java.time.Instant.now;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Slf4j
@RequiredArgsConstructor
public class RegcrowAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private ObjectMapper mapper = new ObjectMapper();


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String email=EMPTY_SPACE_VALUE;
        try {
//            log.info("req body--->{}", request.getInputStream().readLine());
            //1. Extract authentication credentials from the request
            LoginRequest loginRequest = mapper.readValue(request.getInputStream(), LoginRequest.class);
            email = loginRequest.getEmail();
            String password = loginRequest.getPassword();
            //2. Create an Authentication object that isn't yet authenticated
            Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
            //3a. Pass the unauthenticated Authentication object to the authentication manager
            //3b. Get back authenticated authentication object from the authenticationManager
            Authentication authResult = authenticationManager.authenticate(authentication);
            //4. Put the authenticated authentication object in the securityContext
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
        Map<String, String> map = new HashMap<>();
        for (GrantedAuthority authority:authorities) {
            map.put(CLAIM_VALUE, authority.getAuthority());
        }
        return JWT.create()
                .withIssuedAt(now())
                .withExpiresAt(now().plusSeconds(1200L))
                .withClaim(CLAIMS_VALUE, map)
//                .withClaim(CLAIMS_VALUE,authorities.stream()
//                                                    .collect(Collectors
//                                                            .toMap(grantedAuthority->CLAIM_VALUE,
//                                                                    GrantedAuthority::getAuthority)))
                .sign(Algorithm.HMAC512(jwtUtil.getSecret().getBytes()));
    }


}
