package africa.semicolon.regcrow.security.providers;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static africa.semicolon.regcrow.utils.AppUtils.getAuthorities;

@Component
@AllArgsConstructor
public class RegcrowAuthenticationProvider implements AuthenticationProvider {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication authResult;
        String email = (String) authentication.getPrincipal();
        String password =  (String) authentication.getCredentials();
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        String userEmail = userDetails.getUsername();
        String userPassword = userDetails.getPassword();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        if (passwordEncoder.matches(password, userPassword)){
            authResult = new UsernamePasswordAuthenticationToken(userEmail, userPassword, authorities);
            return  authResult;
        }
        throw new BadCredentialsException("Oops!");
    }



    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
