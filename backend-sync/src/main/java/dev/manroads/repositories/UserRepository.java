package dev.manroads.repositories;

import dev.manroads.security.jwt.models.RefreshToken;
import dev.manroads.security.jwt.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUserID(int userID);
    Optional<User> findByUserName(String userName);
}
