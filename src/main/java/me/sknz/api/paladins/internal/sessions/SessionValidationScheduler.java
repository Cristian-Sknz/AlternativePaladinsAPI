package me.sknz.api.paladins.internal.sessions;

import me.sknz.api.paladins.internal.sessions.impl.DefaultSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@EnableScheduling
@Component
public class SessionValidationScheduler {

    private final DefaultSessionFactory sessionFactory;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public SessionValidationScheduler(DefaultSessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Scheduled(fixedRate = 10000)
    protected void checkSessionScheduler() {
        List<ISessionProduct> list = sessionFactory.getAllSessions();
        for (ISessionProduct product : list) {
            OffsetDateTime time = product.getPaladinsSession().getLastUpdate().plusMinutes(15);
            OffsetDateTime now = OffsetDateTime.now(Clock.systemUTC());
            if (product.isInvalid()){
                continue;
            }

            if (now.isAfter(time.minusSeconds(30)) || now.isEqual(time.minusSeconds(30))) {
                try {
                    if (product.testSession()){
                        logger.debug("Sessão {} foi validada com sucesso.", product.getSessionId());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (product.isInvalid()){
                        logger.debug("Não foi possível validar a sessão {}", product.getSessionId());
                    }
                }
            }
        }
    }

    @Scheduled(fixedRate = 15000)
    protected void removeInvalidSessionsScheduler() {
        List<ISessionProduct> list = sessionFactory.getAllSessions()
                .stream().filter(ISessionProduct::isInvalid)
                .collect(Collectors.toList());

        list.forEach((product) -> sessionFactory.removeSession(product.getSessionId(), product.getPaladinsSession().getDeveloper()));
    }
}
