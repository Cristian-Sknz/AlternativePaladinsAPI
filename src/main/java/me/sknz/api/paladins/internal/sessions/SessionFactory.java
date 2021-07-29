package me.sknz.api.paladins.internal.sessions;

import me.sknz.api.paladins.model.PaladinsDeveloper;

import java.io.IOException;
import java.util.Set;

public interface SessionFactory {

    ISessionProduct createSession(PaladinsDeveloper developer) throws IOException;
    ISessionProduct resumeSession(String sessionId, PaladinsDeveloper developer) throws IOException;
    boolean removeSession(String sessionId, PaladinsDeveloper developer);
    Set<ISessionProduct> getActiveSessions(PaladinsDeveloper developer);
}
