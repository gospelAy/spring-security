package africa.semicolon.regcrow.services;

import africa.semicolon.regcrow.models.BioData;
import africa.semicolon.regcrow.repositories.BioDataRepository;
import africa.semicolon.regcrow.security.user.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import static africa.semicolon.regcrow.utils.ExceptionUtils.USER_WITH_EMAIL_NOT_FOUND;

@AllArgsConstructor
@Repository
public class RegcrowBioDataService implements UserDetailsService {
    private final BioDataRepository bioDataRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        BioData bioData = bioDataRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException(
                String.format(USER_WITH_EMAIL_NOT_FOUND, email)
        ));
        return new User(bioData);
    }
}
