package dev.manroads.security.service;

import dev.manroads.payload.TokenRefreshRequest;
import dev.manroads.security.jwt.models.RefreshToken;
import dev.manroads.security.jwt.models.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class RefreshTokenService {

    int refreshExpirationMs;

    public RefreshTokenService(
            @Value("${refreshExpirationMs}") int refreshExpirationMs) {

        this.refreshExpirationMs = refreshExpirationMs;
    }

    /**
     * Method to create a new refresh token
     *
     * @param userDetails : user details of requester
     * @return RefreshToken : RefreshToken class to be returned
     */
    public RefreshToken createRefreshToken(UserDetails userDetails) {

        // Compose a new refresh token
        RefreshToken refreshToken = new RefreshToken();
        User user = new User(userDetails.getUsername(), userDetails.getPassword());

        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.getExpiryDate(new Date(new Date().getTime() + refreshExpirationMs));

        return refreshToken;
    }

    /**
     * Method to verify if current refresh token is still valid
     */
    public boolean verifyRefreshToken(TokenRefreshRequest tokenRefreshRequest) {

        // Retrieve from DB user to which this refresh token belongs
        //if (refreshToken.getExpiryDate().isBefore(new Date().getTime());
        return false;
    }
}
