package dev.manyroads.repositories;

import dev.manyroads.models.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Integer> {
    RefreshToken findByRefreshToken(String refreshToken);
}
