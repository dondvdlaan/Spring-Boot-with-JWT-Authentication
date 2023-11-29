package dev.manroads.security.jwt;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

/**
 * Class with methods for generating, extracting username from and validating a JWT token
 */
@Service
public class JwtUtils {

    final static Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    private final String jwtSecret;
    private final int jwtExpirationMs;

    public JwtUtils(
            @Value("${jwtSecret}") String jwtSecret,
            @Value("${jwtExpirationMs}") int jwtExpirationMs) {

        this.jwtSecret = jwtSecret;
        this.jwtExpirationMs = jwtExpirationMs;
    }
    // ---- Methods ----
    public String generateJWTToken(UserDetails userDetails){
        return generateJWTFromUsername(userDetails.getUsername());
    }

    /**
     * Generate JWT token
     *
     * @param authentication : Authentication object from user login
     * @return String           : jwt token
     */
    public String generateJWTFromUsername(String userNmae) throws WeakKeyException {


        // Compose jwt token
        String jwt = Jwts
                .builder()
                .subject(userNmae)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(generateKey())
                .compact();

        return jwt;
    }

    /**
     * Creates a new SecretKey instance for use with HMAC-SHA algorithms
     *
     * @return Key  : SecretKey instance
     */
    private Key generateKey() {

        // Convert stored jwtSecret in byte array
        byte[] jwtSecretDecodedBytes = Decoders.BASE64.decode(jwtSecret);

        try {
            // Creates a new SecretKey instance for use with HMAC-SHA algorithms
            return Keys.hmacShaKeyFor(jwtSecretDecodedBytes);
        } catch (WeakKeyException ex) {
            logger.error(ex.getMessage());
            throw new WeakKeyException("Key problem");
        }
    }

    /**
     * Retrieve userName from the JWT token
     * @param   String  : token
     * @return  String  : userName
     */
    public String getUserNameFromJwtToken(String token) {

        logger.info("In getUserNameFromJwtToken ");

        return Jwts.parser()
                .setSigningKey(generateKey())
                .build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Validating the JWT token
     * @param authToken : JWT token
     * @return boolean  : true validated / false token not valid
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(generateKey()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

}
