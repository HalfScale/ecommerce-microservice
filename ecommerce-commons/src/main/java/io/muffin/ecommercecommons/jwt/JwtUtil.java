package io.muffin.ecommercecommons.jwt;

import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

@Service
@Slf4j
@Getter
public class JwtUtil {

    private final String secret = "mySecret";
    private final int jwtExpirationMs = 86400000;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Long extractId(String token) {
        return extractClaim(token, (claims) -> claims.get("id", Long.class));
    }

    public Collection<? extends GrantedAuthority> getAuthority(String token) {
        return extractClaim(token, (claims) -> claims.get("authority", Collection.class));
    }

    public String extractTokenFromHeader(String auth) {
        return auth.replace("Bearer ", "");
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(JwtUserDetails user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("authority", user.getAuthorities());
        return createToken(claims, user.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    public boolean validateToken(String token) {
        try {
            Claims claim = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
            String email = claim.getSubject();
            log.info("VALID_TOKEN => [{}]", email);
            return true;
        } catch (SignatureException e) {
            log.error("INVALID_JWT_SIGNATURE => {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("INVALID_JWT_TOKEN => {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT_TOKEN_EXPIRED => {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT_TOKEN_UNSUPPORTED => {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT_CLAIMS_EMPTY => {}", e.getMessage());
        }
        return false;
    }
}
