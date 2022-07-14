package io.muffin.ecommercegateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.muffin.ecommercecommons.jwt.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {


        // 1. get the authentication header. Tokens are supposed to be passed in the authentication header
        String header = request.getHeader("Authorization");
        log.info("REQUESTED_URI => [{}]", request.getRequestURI());

        // 2. validate the header and check the prefix
        if(header == null || !header.startsWith("Bearer ")) {
            log.info("NO_AUTHORIZATION_HEADER");
            chain.doFilter(request, response);  		// If not valid, go to the next filter.
            return;
        }

        // If there is no token provided and hence the user won't be authenticated.
        // It's Ok. Maybe the user accessing a public path or asking for a token.

        // All secured paths that needs a token are already defined and secured in config class.
        // And If a user tried to access without access token, then he won't be authenticated and an exception will be thrown.

        // 3. Get the token
        String token = header.replace("Bearer ", "");
        log.info("AUTH_TOKEN => [{}]", token);

        try{    // exceptions might be thrown in creating the claims if for example the token is expired

            // 4. Validate the token
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtUtil.getSecret())
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();

            if(username != null) {
                // mapping the authority to a List of SimpleGrantedAuthority
                List<LinkedHashMap> jwtAuthorities = claims.get("authority", List.class);
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                jwtAuthorities.forEach(authority -> {
                    authorities.add((new SimpleGrantedAuthority((String) authority.get("authority"))));
                });
                log.info("authority: {}", jwtAuthorities.get(0));
                // 5. Create auth object
                // UsernamePasswordAuthenticationToken: A built-in object, used by spring to represent the current authenticated / being authenticated user.
                // It needs a list of authorities, which has type of GrantedAuthority interface, where SimpleGrantedAuthority is an implementation of that interface
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }catch (Exception ex) {
            log.info("JWT_ERROR => [{}] ", ex.getMessage());
            SecurityContextHolder.clearContext();
        }

        chain.doFilter(request, response);
    }
}
