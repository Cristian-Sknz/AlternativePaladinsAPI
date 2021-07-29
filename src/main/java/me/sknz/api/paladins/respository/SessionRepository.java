package me.sknz.api.paladins.respository;

import me.sknz.api.paladins.model.PaladinsSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<PaladinsSession, String> {
}
