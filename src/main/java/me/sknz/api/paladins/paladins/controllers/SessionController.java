package me.sknz.api.paladins.paladins.controllers;

import me.sknz.api.paladins.internal.IAuthenticationUser;
import me.sknz.api.paladins.internal.sessions.ISessionProduct;
import me.sknz.api.paladins.internal.sessions.SessionFactory;
import me.sknz.api.paladins.model.PaladinsDeveloper;
import me.sknz.api.paladins.model.PaladinsSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Size;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@RestController("paladins-session-controller")
@RequestMapping("api/paladins/sessions")
@Validated
public class SessionController {

    private final IAuthenticationUser authenticationUser;
    private final SessionFactory sessionFactory;

    @Autowired
    public SessionController(IAuthenticationUser authenticationUser, SessionFactory sessionFactory) {
        this.authenticationUser = authenticationUser;
        this.sessionFactory = sessionFactory;
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<Collection<PaladinsSession>> getSessions() {
        Set<PaladinsSession> sessions = sessionFactory.getActiveSessions(authenticationUser.getUserDeveloper())
                .stream().map(ISessionProduct::getPaladinsSession).collect(Collectors.toSet());

        if (sessions.isEmpty()){
            return new ResponseEntity<>(sessions, HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(sessions);
    }

    @GetMapping(value = "/create", produces = "application/json")
    public ResponseEntity<PaladinsSession> createSession() throws IOException {
        PaladinsDeveloper developer = authenticationUser.getUserDeveloper();
        ISessionProduct session = sessionFactory.createSession(developer);

        return ResponseEntity.ok(session.getPaladinsSession());
    }

    @GetMapping(value = "/resume", produces = "application/json")
    public ResponseEntity<PaladinsSession> resumeSession(@RequestParam(name = "sessionId")
                                                             @Size(min = 32, max = 35) String sessionId) throws IOException {
        PaladinsDeveloper developer = authenticationUser.getUserDeveloper();
        ISessionProduct session = sessionFactory.resumeSession(sessionId, developer);
        return ResponseEntity.ok(session.getPaladinsSession());
    }
}
