package com.medical.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.medical.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtil {
    private static final long ACCESS_EXPIRE_DURATION = 24 * 60 * 60 * 1000; // 24 hour

    private static final long REFRESH_EXPIRE_DURATION = 3*24 * 60 * 60 * 1000; // 3 day

    @Value("${app.jwt.secret}")
    private String SECRET_KEY;

    public String generateAccessToken(User user, HttpServletRequest request) {
        Algorithm algorithm= Algorithm.HMAC256(SECRET_KEY.getBytes());
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+ACCESS_EXPIRE_DURATION))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles",user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
    }

    public String generateRefreshToken(User user, HttpServletRequest request) {
        Algorithm algorithm= Algorithm.HMAC256(SECRET_KEY.getBytes());
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+REFRESH_EXPIRE_DURATION))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);
    }

    public DecodedJWT getDecodedJWT(String token) throws Exception{
        Algorithm algorithm= Algorithm.HMAC256(SECRET_KEY.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT=verifier.verify(token);
        return  decodedJWT;
    }

}
