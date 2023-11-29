package dev.manroads.payload;

import java.util.ArrayList;
import java.util.List;

public class JwtResponse {

    String token;
    String refreshToken;
    String userName;
    List<String> roles = new ArrayList<>();

    public JwtResponse(String token, String refreshToken, String userName, List<String> roles) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.userName = userName;
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
