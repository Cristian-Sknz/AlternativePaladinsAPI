package me.sknz.api.paladins.internal.impl;

import me.sknz.api.paladins.respository.PaladinsDeveloperRepository;
import me.sknz.api.paladins.authentication.model.APIUserModel;
import me.sknz.api.paladins.respository.APIUserRepository;
import me.sknz.api.paladins.internal.IAuthenticationUser;
import me.sknz.api.paladins.model.PaladinsDeveloper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationUser implements IAuthenticationUser {

    @Autowired
    private APIUserRepository apiUserRepository;
    @Autowired
    private PaladinsDeveloperRepository developerRepository;


    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public APIUserModel getUser() {
        return apiUserRepository.getById(Integer.parseInt(getAuthentication().getName()));
    }

    @Override
    public PaladinsDeveloper getUserDeveloper() {
        return getUser().getDeveloper();
    }

    @Override
    public void save(PaladinsDeveloper developer) {
        developerRepository.save(developer);
    }
}
