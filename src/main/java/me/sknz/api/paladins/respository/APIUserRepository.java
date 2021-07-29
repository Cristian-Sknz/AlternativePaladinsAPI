package me.sknz.api.paladins.respository;

import me.sknz.api.paladins.authentication.model.APIUserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface APIUserRepository extends JpaRepository<APIUserModel, Integer> {
}
