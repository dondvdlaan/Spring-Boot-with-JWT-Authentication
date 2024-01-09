package dev.manyroads.payloads;

/*
Payload to receive the Refresh token from the body in the message
 */
public record TokenRefreshRequest(
        String refreshToken
) {

    @Override
    public String toString() {
        return "TokenRefreshRequest{" +
                "refreshToken='" + refreshToken + '\'' +
                '}';
    }
}
