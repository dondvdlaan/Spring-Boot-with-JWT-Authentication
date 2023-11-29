package dev.manroads.security.jwt.models;

import java.time.Instant;
import java.util.Date;

public class RefreshToken {

    User user;
    String token;
    Instant expiryDate;

    public RefreshToken() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Instant getExpiryDate(Date date) {
        return expiryDate;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }
}
