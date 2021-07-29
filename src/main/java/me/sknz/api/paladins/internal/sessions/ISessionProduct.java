package me.sknz.api.paladins.internal.sessions;

import com.fasterxml.jackson.databind.JsonNode;
import me.sknz.api.paladins.model.PaladinsSession;

import java.io.IOException;

public interface ISessionProduct {

    JsonNode request(String method, Object... arguments) throws IOException;
    boolean testSession() throws IOException;
    PaladinsSession getPaladinsSession();

    default String getSessionId(){
        return getPaladinsSession().getSessionId();
    }
}