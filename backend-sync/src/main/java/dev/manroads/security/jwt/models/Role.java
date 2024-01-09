package dev.manroads.security.jwt.models;

import jakarta.persistence.*;

/**
 * Class used for authorization
 */
@Entity
public class Role {

    @Id
    @GeneratedValue
    long roleID;
    @Enumerated(EnumType.STRING)
    ERole role;

    public Role() {
    }

    public Role(ERole role) {
        this.role = role;
    }

    public long getRoleID() {
        return roleID;
    }

    public void setRoleID(long roleID) {
        this.roleID = roleID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }

    public ERole getRole() {
        return role;
    }

    public void setRole(ERole role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Role{" +
                "roleID=" + roleID +
                ", role=" + role +
                '}';
    }
}
