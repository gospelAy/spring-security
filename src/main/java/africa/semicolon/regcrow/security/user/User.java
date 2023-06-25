package africa.semicolon.regcrow.security.user;

import africa.semicolon.regcrow.models.BioData;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


@AllArgsConstructor
public class User implements UserDetails {
    private final BioData bioData;

    @Override
    public String getUsername() {
        return bioData.getEmail();
    }

    @Override
    public String getPassword() {
        return bioData.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return bioData.getRoles().stream()
                .map(role->new SimpleGrantedAuthority(role.name()))
                .toList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return bioData.getIsEnabled();
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


}
