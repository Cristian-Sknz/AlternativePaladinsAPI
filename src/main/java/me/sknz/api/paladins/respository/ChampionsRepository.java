package me.sknz.api.paladins.respository;

import me.sknz.api.paladins.model.DatabaseChampions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChampionsRepository extends JpaRepository<DatabaseChampions, Integer> {
}
