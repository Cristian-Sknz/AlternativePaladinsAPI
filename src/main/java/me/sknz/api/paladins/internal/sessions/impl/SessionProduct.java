package me.sknz.api.paladins.internal.sessions.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.sknz.api.paladins.internal.sessions.ISessionProduct;
import me.sknz.api.paladins.model.PaladinsDeveloper;
import me.sknz.api.paladins.model.PaladinsSession;
import me.sknz.api.paladins.paladins.PaladinsAPIUtil;
import me.sknz.api.paladins.respository.SessionRepository;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
import java.util.Objects;

public class SessionProduct implements ISessionProduct {

    private final OkHttpClient client;
    private final PaladinsDeveloper developer;
    private final SessionRepository repository;

    private final PaladinsSession session;
    private boolean isValid;

    public SessionProduct(PaladinsDeveloper developer, PaladinsSession session,
                          SessionRepository repository, OkHttpClient client) {
        this.developer = developer;
        this.repository = repository;
        this.session = session;
        this.client = client;
        this.isValid = true;
    }

    @Override
    public JsonNode request(String method, Object... arguments) throws IOException {
        Request request = new Request.Builder()
                .url(PaladinsAPIUtil.formatURL(method, developer.getDevId(), developer.getAuthKey(), session.getSessionId(), arguments))
                .build();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(Objects.requireNonNull(client.newCall(request).execute().body(), "session request response is null").string());
        if (node.get("ret_msg").isNull()){
            repository.save(session.update());
            return node;
        }

        // TODO adicionar um sistema de verifição de resposta.
        throw new IOException("Erro ao fazer uma solicitação");
    }

    @Override
    public boolean testSession() throws IOException {
        Request request = new Request.Builder()
                .url(PaladinsAPIUtil.formatURL("testsession", developer.getDevId(), developer.getAuthKey(), session.getSessionId()))
                .build();

        String response = Objects.requireNonNull(client.newCall(request).execute().body(), "test session response is null").string();
        if (response.contains("successful test")) {
            repository.save(session.update());
            return this.isValid = true;
        }
        return this.isValid = false;
    }

    @Override
    public boolean isInvalid() {
        return !isValid;
    }

    @Override
    public PaladinsSession getPaladinsSession() {
        return session;
    }
}
