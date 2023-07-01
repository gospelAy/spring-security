package africa.semicolon.regcrow.utils;


import africa.semicolon.regcrow.exceptions.RegCrowException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

import static africa.semicolon.regcrow.utils.AppUtils.CLAIMS_VALUE;

@AllArgsConstructor
@Getter
public class JwtUtil {
    private final String secret;

    public Map<String, Claim> extractClaimsFrom(String token) throws RegCrowException {
        validateToken(token);
        DecodedJWT decodedJwt = validateToken(token);
        if (decodedJwt.getClaim(CLAIMS_VALUE)==null) throw new RegCrowException("");
        return decodedJwt.getClaims();
    }

    private DecodedJWT validateToken(String token) {
        return JWT.require(Algorithm.HMAC512(secret))
                .build().verify(token);
    }

}
