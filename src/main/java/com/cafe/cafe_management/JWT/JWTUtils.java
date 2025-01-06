package com.cafe.cafe_management.JWT;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.impl.JWTParser;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTUtils {
    private static final String SECRET = "CafeJavaThisShouldBeMoreThan32Characters";

    public String extractUsername(String token){
        return extractClaims(token, DecodedJWT::getSubject);
    }

    public Date extractExpiration(String token){
        return extractClaims(token, DecodedJWT::getExpiresAt);
    }

    public <T> T extractClaims(String token, Function<DecodedJWT, T> claimsresolver){
        final DecodedJWT claims = extractAllClaims(token);
        if(claims!=null){
            return claimsresolver.apply(claims);
        }else{
            return null;
        }
    }

    public DecodedJWT extractAllClaims(String token){
        DecodedJWT decodedJWT;
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            decodedJWT = verifier.verify(token);
            return decodedJWT;
        }catch (JWTVerificationException exception){
            exception.printStackTrace();
            System.out.println("Invalid signature/claims.");
        }
        return null;
    }

    private boolean isTokenExpired(String token){
        Date date =  extractExpiration(token);
        if(date!=null){
            return date.before(new Date());
        }else{
            return true;
        }
    }

    public String generateToken(String username, String role){
        Map<String, Object> claims =  new HashMap<>();
        claims.put("role", role);
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject){
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            String token = JWT.create().withSubject(subject).withPayload(claims)
                    .withIssuedAt(new Date(System.currentTimeMillis()))
                    .withExpiresAt(new Date(System.currentTimeMillis()+1000*60*60*10))
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception){
            System.out.println("Invalid Signing configuration / Couldn't convert Claims.");
            return null;
        }
    }

    public boolean validateToken(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        if (username !=null) {
            return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        }else{
            return false;
        }
    }
}
