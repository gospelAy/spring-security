package africa.semicolon.regcrow.security.filters;

import africa.semicolon.regcrow.exceptions.RegCrowException;
import africa.semicolon.regcrow.utils.AppUtils;
import africa.semicolon.regcrow.utils.JwtUtil;
import com.auth0.jwt.interfaces.Claim;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static africa.semicolon.regcrow.utils.AppUtils.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@Component
@RequiredArgsConstructor
@Slf4j
public class RegcrowAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
            if (AppUtils.getAuthWhiteList().contains(request.getServletPath())&&
                    request.getMethod().equals(HttpMethod.POST.name())){
                filterChain.doFilter(request,response);
            }else {
                authorize(request);
                log.info("HERE");
                filterChain.doFilter(request, response);
            }

    }

    private void authorize(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        boolean isValidAuthorizationHeader = authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX);
        if(isValidAuthorizationHeader) {
            try {
                parseTokenFrom(authorizationHeader);
            }catch (Exception exception){
                log.error("ERROR::{}", exception.getMessage());
            }
        }
    }

    private void parseTokenFrom(String authorizationHeader) {
        String token = authorizationHeader.substring(TOKEN_PREFIX.length());
        try {
            Map<String,Claim> map = jwtUtil.extractClaimsFrom(token);
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            Claim claim = map.get(CLAIMS_VALUE);
            buildAuthority(authorities, claim);
            log.info("authorities->{}", authorities);
            Authentication authentication = new UsernamePasswordAuthenticationToken(null, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (RegCrowException e) {
            log.error("error-->{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static void buildAuthority(List<SimpleGrantedAuthority> authorities, Claim claim) {
        String role = claim.asMap().get(CLAIM_VALUE).toString();
        authorities.add(new SimpleGrantedAuthority(role));
    }


}
