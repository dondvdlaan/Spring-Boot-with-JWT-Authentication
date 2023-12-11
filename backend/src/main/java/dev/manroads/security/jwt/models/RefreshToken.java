package dev.manroads.security.jwt.models;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.Date;

@Entity
public class
RefreshToken {

    @Id
    @GeneratedValue
    private int refreshTokenID;
    private String refreshToken;

    public RefreshToken() {
    }

    public RefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    int getRefreshTokenID() {
        return refreshTokenID;
    }

    public void setRefreshTokenID(int refreshTokenID) {
        this.refreshTokenID = refreshTokenID;
    }


    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }


    @Override
    public String toString() {
        return "RefreshToken{" +
                "refreshTokenID=" + refreshTokenID +
                ", refreshToken='" + refreshToken + '\'' +
                '}';
    }
}
