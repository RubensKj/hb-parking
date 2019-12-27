package br.com.hbparking.security.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserAuthServiceImpl implements UserDetailsService {

    private final IUserRepository iUserRepository;

    @Autowired
    public UserAuthServiceImpl(IUserRepository iUserRepository) {
        this.iUserRepository = iUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return UserAuthPrincipal.build(iUserRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Usuário não foi encontrado com esse e-mail - " + email)));
    }
}
