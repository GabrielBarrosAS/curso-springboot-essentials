package lead.mentoring.springboot2.service;

import lead.mentoring.springboot2.repository.SpringbootEssentialsUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class SpringbootEssentialsUserService implements UserDetailsService {

    private final SpringbootEssentialsUserRepository springbootEssentialsUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info(springbootEssentialsUserRepository.findByUsername(username));
        log.info(username);
        return Optional.ofNullable(springbootEssentialsUserRepository.findByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não cadastrado"));
    }
}
