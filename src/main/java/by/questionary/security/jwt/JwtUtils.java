package by.questionary.security.jwt;

import by.questionary.security.entity.UserDetailsImpl;
import io.jsonwebtoken.*;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
@NoArgsConstructor
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration.ms}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        String subject = userPrincipal.getUsername();
        Date issuedDate = new Date();
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;
        long currentTime = new Date().getTime();
        long expirationTime = currentTime + jwtExpirationMs;
        Date expiration = new Date(expirationTime);

        return Jwts.builder().
                setSubject(subject)
                .setIssuedAt(issuedDate)
                .setExpiration(expiration)
                .signWith(signatureAlgorithm, jwtSecret)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {

        boolean result = false;

        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            result = true;
        } catch (SignatureException e) {
            log.error("Wrong JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Wrong JWT Token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT Token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT Token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty: {}", e.getMessage());
        }

        return result;
    }

}
