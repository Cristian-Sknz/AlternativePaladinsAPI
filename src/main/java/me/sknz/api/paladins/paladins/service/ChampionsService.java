package me.sknz.api.paladins.paladins.service;

import com.fasterxml.jackson.databind.JsonNode;
import me.sknz.api.paladins.exception.PaladinsAPIException;
import me.sknz.api.paladins.internal.sessions.ISessionProduct;
import me.sknz.api.paladins.internal.sessions.SessionFactory;
import me.sknz.api.paladins.model.DatabaseChampions;
import me.sknz.api.paladins.model.PaladinsDeveloper;
import me.sknz.api.paladins.paladins.models.APILanguage;
import me.sknz.api.paladins.paladins.models.champions.Champion;
import me.sknz.api.paladins.paladins.models.champions.Champions;
import me.sknz.api.paladins.respository.ChampionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

@Service
public class ChampionsService {

    private final ChampionsRepository repository;
    private final SessionFactory sessionFactory;

    @Autowired
    public ChampionsService(ChampionsRepository repository, SessionFactory sessionFactory) {
        this.repository = repository;
        this.sessionFactory = sessionFactory;
    }

    public Champions getChampions(PaladinsDeveloper developer, APILanguage language) throws IOException {
        Optional<DatabaseChampions> optional = repository.findById(language.getValue());
        if (optional.isPresent()) {
            return optional.get().getChampions();
        }

        Set<ISessionProduct> sessions = sessionFactory.getActiveSessions(developer);
        if (sessions.isEmpty()) {
            throw new PaladinsAPIException("Você precisa ter uma sessão ativa para fazer uma solicitação!");
        }
        ISessionProduct session = sessions.iterator().next();
        JsonNode node = session.request("getchampions", language.getValue());

        return repository.save(new DatabaseChampions(language, node.toString())).getChampions();
    }

    public Champion getChampion(String name, PaladinsDeveloper developer, APILanguage language) throws IOException {
        Optional<DatabaseChampions> databaseChampions = repository.findById(language.getValue());
        if (databaseChampions.isPresent()) {
            return databaseChampions.get().getChampion(name)
                    .orElseThrow(() -> new BadCredentialsException("This requested champion does not exist or is incorrect."));
        }

        return createDatabaseChampion(language, developer).getChampion(name)
                .orElseThrow(() -> new BadCredentialsException("This requested champion does not exist or is incorrect."));
    }

    public DatabaseChampions createDatabaseChampion(APILanguage language, PaladinsDeveloper paladinsDeveloper) throws IOException {
        Set<ISessionProduct> sessions = sessionFactory.getActiveSessions(paladinsDeveloper);
        if (sessions.isEmpty()) {
            throw new PaladinsAPIException("Você precisa ter uma sessão ativa para fazer uma solicitação!");
        }
        ISessionProduct session = sessions.iterator().next();
        JsonNode node = session.request("getchampions", language.getValue());

        return repository.save(new DatabaseChampions(language, node.toString()));
    }
}