package me.sknz.api.paladins.internal.sessions.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.sknz.api.paladins.internal.sessions.ISessionProduct;
import me.sknz.api.paladins.internal.sessions.SessionFactory;
import me.sknz.api.paladins.model.PaladinsDeveloper;
import me.sknz.api.paladins.model.PaladinsSession;
import me.sknz.api.paladins.paladins.APIMessage;
import me.sknz.api.paladins.respository.PaladinsDeveloperRepository;
import me.sknz.api.paladins.paladins.PaladinsAPIUtil;
import me.sknz.api.paladins.respository.SessionRepository;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DefaultSessionFactory implements SessionFactory {

    private final PaladinsDeveloperRepository developerRepository;
    private final SessionRepository sessionRepository;
    private final OkHttpClient client;

    private final List<ISessionProduct> sessionProducts = new ArrayList<>();

    @Autowired
    public DefaultSessionFactory(PaladinsDeveloperRepository developerRepository,
                                 SessionRepository sessionRepository, OkHttpClient client) {
        this.developerRepository = developerRepository;
        this.sessionRepository = sessionRepository;
        this.client = client;
    }

    @Override
    public ISessionProduct createSession(PaladinsDeveloper developer) throws IOException {
        Call call = client.newCall(new Request.Builder()
                .url(PaladinsAPIUtil.formatURL("createsession", developer.getDevId(),
                        developer.getAuthKey(), null))
                .build());

        JsonNode node = new ObjectMapper().readTree(call.execute().body().string());
        if (APIMessage.isValidResponse(node.get("ret_msg").asText())) {
            PaladinsSession session = new PaladinsSession(node.get("session_id").asText(), developer);
            developer.addSession(session);
            developerRepository.save(developer);

            ISessionProduct product = createProduct(session);
            sessionProducts.add(product);
            return product;
        }
        // TODO Adicionar uma exceção descente
        throw new RuntimeException("Não foi possivel criar uma sessão");
    }

    @Override
    public ISessionProduct resumeSession(String sessionId, PaladinsDeveloper developer) throws IOException {
        if (sessionRepository.existsById(sessionId)){
            throw new RuntimeException("Esta sessão já existe no banco de dados");
            // TODO lançar uma exceção descente
        }
        ISessionProduct product = createProduct(new PaladinsSession(sessionId, developer));
        if (product.testSession()){
            developer.addSession(product.getPaladinsSession());
            developerRepository.save(developer);
            this.sessionProducts.add(product);
        }
        // TODO lançar uma exceção descente
        throw new RuntimeException("Não foi possivel criar uma sessão");
    }

    @Override
    public Set<ISessionProduct> getActiveSessions(PaladinsDeveloper developer) {
        if (developer.getSessions().isEmpty()){
            return new HashSet<>();
        }

        return developer.getSessions().stream().map(this::createProduct).collect(Collectors.toSet());
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
