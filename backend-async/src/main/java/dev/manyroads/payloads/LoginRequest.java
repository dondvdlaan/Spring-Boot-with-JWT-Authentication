package dev.manyroads.payloads;

/**
 * This payload class receives the user details of a login
 */
public record LoginRequest

        (String userName,
         String passWord) {

    @Override
    public String toString() {
        return "LoginRequest: " +
                "userName = " + userName +
                ", passWord = " + passWord;
    }
}
