package dev.manyroads.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Document
public class User {

    @Id
    String userID;
    String userName;
    String passWord;
    Set<Role> roles = new HashSet<>();

    public User() {
    }

    public User(String userID, String userName, String passWord, Set<Role> roles) {
        this.userID = userID;
        this.userName = userName;
        this.passWord = passWord;
        this.roles = roles;
    }

    public User(String userName, String passWord, Set<Role> roles) {
        this.userName = userName;
        this.passWord = passWord;
        this.roles = roles;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "userID='" + userID + '\'' +
                ", userName='" + userName + '\'' +
                ", passWord='" + passWord + '\'' +
                ", roles=" + roles +
                '}';
    }
}
