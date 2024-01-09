package dev.manyroads.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

/*
Class is refresh token entity for storing in MySQL DB
 */
@Entity
public class RefreshToken {

    @Id
    @GeneratedValue
    int refreshTokenID;

    String refreshToken;

    public RefreshToken() {
    }

    public RefreshToken(int refreshTokenID, String refreshToken) {
        this.refreshTokenID = refreshTokenID;
        this.refreshToken = refreshToken;
    }

    public RefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public int getRefreshTokenID() {
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
                "refershTokenID='" + refreshTokenID + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                '}';
    }
}
