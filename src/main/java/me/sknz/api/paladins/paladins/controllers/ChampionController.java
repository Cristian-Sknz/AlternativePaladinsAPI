package me.sknz.api.paladins.paladins.controllers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import me.sknz.api.paladins.exception.PaladinsAPIException;
import me.sknz.api.paladins.internal.IAuthenticationUser;
import me.sknz.api.paladins.internal.sessions.ISessionProduct;
import me.sknz.api.paladins.internal.sessions.SessionFactory;
import me.sknz.api.paladins.paladins.models.APILanguage;
import me.sknz.api.paladins.paladins.models.champions.*;
import me.sknz.api.paladins.paladins.service.ChampionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController("paladins-champion-controller")
@RequestMapping("api/paladins/champions")
public class ChampionController {

    private final ChampionsService championsService;
    private final IAuthenticationUser authenticationUser;
    private final SessionFactory sessionFactory;

    @Autowired
    public ChampionController(ChampionsService championsService, IAuthenticationUser authenticationUser, SessionFactory sessionFactory) {
        this.championsService = championsService;
        this.authenticationUser = authenticationUser;
        this.sessionFactory = sessionFactory;
    }

    @GetMapping(value = "")
    public ResponseEntity<Champions> getChampions(@RequestParam(name = "language",
            required = false,
            defaultValue = "english") String language) throws IOException {
        return ResponseEntity.ok(championsService.getChampions(authenticationUser.getUserDeveloper(), APILanguage.getByValue(language)));
    }

    @GetMapping(value = "/{champion}")
    public ResponseEntity<Champion> getChampion(@PathVariable(name = "champion") String champion,
                                                @RequestParam(name = "language", required = false,
            defaultValue = "english") String language) throws IOException {
        return ResponseEntity.ok(championsService.getChampion(champion, authenticationUser.getUserDeveloper(), APILanguage.getByValue(language)));
    }

    @GetMapping(value = "/skins")
    public ResponseEntity<Object> getChampionSkins(@RequestParam(name = "language",
            required = false,
            defaultValue = "english") String language) throws IOException {
        Set<ISessionProduct> sessions = sessionFactory.getActiveSessions(authenticationUser.getUserDeveloper());
        if (sessions.isEmpty()) {
            throw new PaladinsAPIException("Você precisa ter uma sessão ativa para fazer uma solicitação!");
        }

        APILanguage lang = APILanguage.getByValue(language);
        ISessionProduct session = sessions.iterator().next();
        Champions champions = championsService.getChampions(authenticationUser.getUserDeveloper(), lang);
        List<ChampionSkins> skins = ChampionSkins.from(champions, (ArrayNode) session.request("getchampionskins", -1, lang.getValue()));
        return ResponseEntity.ok(skins);
    }

    @GetMapping(value = "/{champion}/skins")
    public ResponseEntity<ChampionSkins> getChampionSkins(@PathVariable(name = "champion") String champion,
                                                          @RequestParam(name = "language",
                                                                  required = false,
                                                                  defaultValue = "english") String language) throws IOException {
        Set<ISessionProduct> sessions = sessionFactory.getActiveSessions(authenticationUser.getUserDeveloper());
        if (sessions.isEmpty()) {
            throw new PaladinsAPIException("Você precisa ter uma sessão ativa para fazer uma solicitação!");
        }
        APILanguage lang = APILanguage.getByValue(language);
        ChampionShort championShort = championsService.getChampion(champion, authenticationUser.getUserDeveloper(), lang);
        ISessionProduct session = sessions.iterator().next();
        ChampionSkins skins = new ChampionSkins(championShort, (ArrayNode) session.request("getchampionskins", championShort.id, lang.getValue()));
        return ResponseEntity.ok(skins);
    }

    @GetMapping(value = "/{champion}/cards")
    public ResponseEntity<ChampionCards> getChampionCards(@PathVariable(name = "champion") String champion,
                                                          @RequestParam(name = "language",
                                                                  required = false,
                                                                  defaultValue = "english") String language) throws IOException {
        Set<ISessionProduct> sessions = sessionFactory.getActiveSessions(authenticationUser.getUserDeveloper());
        if (sessions.isEmpty()) {
            throw new PaladinsAPIException("Você precisa ter uma sessão ativa para fazer uma solicitação!");
        }

        APILanguage lang = APILanguage.getByValue(language);
        ChampionShort championShort = championsService.getChampion(champion, authenticationUser.getUserDeveloper(), lang);
        ISessionProduct session = sessions.iterator().next();
        return ResponseEntity.ok(new ChampionCards(championShort, (ArrayNode) session.request("getchampioncards",
                        championShort.id, lang.getValue())));
    }

    @GetMapping(value = "/cards")
    public ResponseEntity<Object> getChampionCards(@RequestParam(name = "language",
            required = false,
            defaultValue = "english") String language) throws IOException {
        Set<ISessionProduct> sessions = sessionFactory.getActiveSessions(authenticationUser.getUserDeveloper());
        if (sessions.isEmpty()) {
            throw new PaladinsAPIException("Você precisa ter uma sessão ativa para fazer uma solicitação!");
        }

        APILanguage lang = APILanguage.getByValue(language);
        ISessionProduct session = sessions.iterator().next();
        Champions champions = championsService.getChampions(authenticationUser.getUserDeveloper(), lang);
        List<ChampionCards> cards = ChampionCards.from(champions, (ArrayNode) session.request("getchampioncards", -1, lang.getValue()));
        return ResponseEntity.ok(cards);
    }
}
