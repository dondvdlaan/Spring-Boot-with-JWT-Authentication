package dev.manroads.security.jwt.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Class used for authentication of user
 */
@Entity
public class User {

    @Id
    @GeneratedValue
    Long userID;
    String userName;
    String passWord;
    //String email;
    // Authorization roles
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(	name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    Set<Role> roles = new HashSet<>();

    public User() {
    }

    public User(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
    }

    public User(String userName, String passWord, Set<Role> roles) {
        this.userName = userName;
        this.passWord = passWord;
        this.roles = roles;
    }

    public long getUserID() {
        return userID;
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

    public void setUserID(Long userID) {
        this.userID = userID;
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
                "userID=" + userID +
                ", userName='" + userName + '\'' +
                ", passWord='" + passWord + '\'' +
                ", roles=" + roles +
                '}';
    }
}
