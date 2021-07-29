package me.sknz.api.paladins.authentication.data;

import me.sknz.api.paladins.respository.APIUserRepository;
import me.sknz.api.paladins.authentication.model.APIUserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JWTUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private APIUserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<APIUserModel> optional = repository.findById(Integer.parseInt(username));
        if (optional.isPresent()){
            return optional.get();
        }
        throw new UsernameNotFoundException("Usuário não foi encontrado!");
    }

    public APIUserRepository getRepository() {
        return repository;
    }
}
