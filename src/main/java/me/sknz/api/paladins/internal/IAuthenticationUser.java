package me.sknz.api.paladins.internal;

import me.sknz.api.paladins.model.PaladinsDeveloper;
import me.sknz.api.paladins.authentication.model.APIUserModel;
import org.springframework.security.core.Authentication;

public interface IAuthenticationUser {

    Authentication getAuthentication();
    APIUserModel getUser();
    PaladinsDeveloper getUserDeveloper();
    void save(PaladinsDeveloper developer);
}
