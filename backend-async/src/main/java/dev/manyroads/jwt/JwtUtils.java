package dev.manyroads.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.security.Key;
import java.util.Collection;
import java.util.Date;

/*
Class to offer different methods for the jwt token
 */
@Component
public class JwtUtils {

    final static Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    private final String jwtSecret;

    public JwtUtils(@Value("${jwtSecret}") String jwtSecret) {

        this.jwtSecret = jwtSecret;
    }

    public String generateJWT(
            String userName,
            int jwtExpirationMs)
            throws WeakKeyException {

        // Compose jwt token
        String jwt = Jwts
                .builder()
                .subject(userName)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(generateKey())
                .compact();

        return jwt;
    }

    /**
     * Validating the JWT token
     *
     * @param authToken : JWT token
     * @return boolean  : true validated / false token not valid
     */
    public boolean validateJwtToken(String authToken) throws ExpiredJwtException {

        logger.info("Validating token");

        try {
            Jwts
                    .parser()
                    .setSigningKey(generateKey())
                    .build()
                    .parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("JWT validation failed: {}", e.getMessage());
        }

        return false;
    }

    /*
    Creates new authentication needed after successful authentication
     */
    public Authentication getAuthentication(String token) {

        Claims claims = null;

        try {
            claims = Jwts
                    .parser()
                    .setSigningKey(generateKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            logger.error("Claims conversion failed: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

        logger.debug("claims: ", claims);

        Object authoritiesClaim = claims.get("roles");

        Collection<? extends GrantedAuthority> authorities = authoritiesClaim == null
                ? AuthorityUtils.NO_AUTHORITIES
                : AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesClaim.toString());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    // **** Sub-methods ****

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
     *
     * @param String : token
     * @return String  : userName
     */
    public String getUserNameFromJwtToken(String token) {

        logger.info("In getUserNameFromJwtToken ");

        return Jwts.parser()
                .setSigningKey(generateKey())
                .build()
                .parseClaimsJws(token).getBody().getSubject();
    }
}
