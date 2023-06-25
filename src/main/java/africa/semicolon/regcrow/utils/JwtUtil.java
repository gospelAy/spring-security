package africa.semicolon.regcrow.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

import static java.time.Instant.now;

@AllArgsConstructor
@Getter
public class JwtUtil {
    private final String secret;


}
