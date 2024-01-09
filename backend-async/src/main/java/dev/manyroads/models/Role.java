package dev.manyroads.models;



public class Role {
    ERole role;

    public Role() {
    }

    public Role(ERole role) {
        this.role = role;
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
                "role=" + role +
                '}';
    }
}
