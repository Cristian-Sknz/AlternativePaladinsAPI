package me.sknz.api.paladins.respository;

import me.sknz.api.paladins.model.PaladinsDeveloper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaladinsDeveloperRepository extends JpaRepository<PaladinsDeveloper, Integer> {
}
