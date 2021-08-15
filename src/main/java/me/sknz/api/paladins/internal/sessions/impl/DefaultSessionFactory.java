package me.sknz.api.paladins.internal.sessions.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.sknz.api.paladins.exception.PaladinsAPIException;
import me.sknz.api.paladins.internal.sessions.ISessionProduct;
import me.sknz.api.paladins.internal.sessions.SessionFactory;
import me.sknz.api.paladins.model.PaladinsDeveloper;
import me.sknz.api.paladins.model.PaladinsSession;
import me.sknz.api.paladins.paladins.APIMessage;
import me.sknz.api.paladins.paladins.PaladinsAPIUtil;
import me.sknz.api.paladins.respository.PaladinsDeveloperRepository;
import me.sknz.api.paladins.respository.SessionRepository;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DefaultSessionFactory implements SessionFactory {

    private final PaladinsDeveloperRepository developerRepository;
    private final SessionRepository sessionRepository;
    private final OkHttpClient client;
    private final Logger logger = LoggerFactory.getLogger(DefaultSessionFactory.class);
    private final MessageSource source;

    private final List<ISessionProduct> sessionProducts = new ArrayList<>();

    @Autowired
    public DefaultSessionFactory(PaladinsDeveloperRepository developerRepository,
                                 SessionRepository sessionRepository,
                                 OkHttpClient client, MessageSource source) {
        this.developerRepository = developerRepository;
        this.sessionRepository = sessionRepository;
        this.client = client;
        this.source = source;
    }

    @Override
    public ISessionProduct createSession(PaladinsDeveloper developer) throws IOException {
        Call call = client.newCall(new Request.Builder()
                .url(PaladinsAPIUtil.formatURL("createsession", developer.getDevId(),
                        developer.getAuthKey(), null))
                .build());

        JsonNode node = new ObjectMapper().readTree(Objects.requireNonNull(call.execute().body(), "create session response is null").string());
        if (APIMessage.isValidResponse(node.get("ret_msg").asText())) {
            PaladinsSession session = new PaladinsSession(node.get("session_id").asText(), developer);
            developer.addSession(session);
            developerRepository.save(developer);

            ISessionProduct product = createProduct(session);
            sessionProducts.add(product);
            return product;
        }

        throw new PaladinsAPIException(node.get("ret_msg").asText());
    }

    @Override
    public ISessionProduct resumeSession(String sessionId, PaladinsDeveloper developer) throws IOException {
        if (sessionRepository.existsById(sessionId)) {
            throw new RuntimeException(source.getMessage("exception.api.session.alreadyexists", null, Locale.getDefault()));
        }
        ISessionProduct product = createProduct(new PaladinsSession(sessionId, developer));
        if (product.testSession()){
            developer.addSession(product.getPaladinsSession());
            developerRepository.save(developer);
            this.sessionProducts.add(product);
        }
        throw new PaladinsAPIException(source.getMessage("exception.api.session.invalidresume", null, Locale.getDefault()));
    }

    private void resumeDatabaseSessions() {
        List<PaladinsDeveloper> developers = developerRepository.findAll()
                .stream().filter(developer -> !developer.getSessions().isEmpty())
                .collect(Collectors.toList());

        for (PaladinsDeveloper dev : developers) {
            for (PaladinsSession session : dev.getSessions()) {
                if (sessionProducts.stream().noneMatch(product -> product.getSessionId()
                        .equalsIgnoreCase(session.getSessionId()))) {
                    try {
                        ISessionProduct product = createProduct(session);
                        if (product.testSession()) {
                            this.sessionProducts.add(product);
                            continue;
                        }
                        dev.getSessions().removeIf(s -> {
                            if (s.getSessionId().equalsIgnoreCase(product.getSessionId())) {
                                logger.warn("Não foi possível reassumir a sessão {}", s.getSessionId());
                                // TODO MessageBundle
                                return true;
                            }
                            return false;
                        });
                        developerRepository.save(dev);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public boolean removeSession(String sessionId, PaladinsDeveloper developer) {
        boolean remove = developer.getSessions().removeIf(session -> session.getSessionId().equalsIgnoreCase(sessionId));
        sessionProducts.removeIf(session -> session.getSessionId().equalsIgnoreCase(sessionId));

        developerRepository.save(developer);
        return remove;
    }

    @Override
    public Set<ISessionProduct> getActiveSessions(PaladinsDeveloper developer) {
        if (developer.getSessions().isEmpty()){
            return new HashSet<>();
        }

        return developer.getSessions().stream().map(this::createProduct).collect(Collectors.toSet());
    }

    @EventListener
    protected void initActiveSessions(ContextRefreshedEvent event) {
        this.logger.info("Iniciando todas as sessões no banco de dados.");
        this.resumeDatabaseSessions();
    }

    private ISessionProduct createProduct(PaladinsSession paladinsSession){
        Optional<ISessionProduct> existentProduct = sessionProducts.stream()
                .filter(product -> product.getPaladinsSession().getSessionId()
                        .equalsIgnoreCase(paladinsSession.getSessionId()))
                .findFirst();

        return existentProduct.orElseGet(() -> new SessionProduct(paladinsSession.getDeveloper(), paladinsSession, sessionRepository, client));
    }

    public List<ISessionProduct> getAllSessions() {
        return sessionProducts;
    }
}
