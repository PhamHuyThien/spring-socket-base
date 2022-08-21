package com.thiendz.example.springsocket.auths;


import com.thiendz.example.springsocket.model.UserProfile;
import com.thiendz.example.springsocket.repo.jpa.UserProfileRepository;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
public class JwtTokenProvider {

    public static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    public static final String AUTHORIZATION_HEADER_VALUE_PREFIX = "Bearer ";

    private static final long EXPIRED_TIME = 86_400_000L;
    private static final long ADMIN_EXPIRED_TIME = 311_040_000_000L;

    @Autowired
    UserProfileRepository userProfileRepository;

//    @Value("${vikingdom.center.secret}")
    String SECRET = "ADMIN_TEST";

    public String generateToken(UserProfile userProfile) {
        Date datePresent = new Date();
        Date dateExpired = new Date(datePresent.getTime() + EXPIRED_TIME);
        return Jwts.builder()
                .setSubject(userProfile.getUsername())
                .claim("type", AUTHORIZATION_HEADER_KEY)
                .claim("prefix", AUTHORIZATION_HEADER_VALUE_PREFIX.trim())
                .claim("author", "SystemError")
                .claim("contact", "SystemError.Dev@gmail.com")
                .claim("expiredTime",EXPIRED_TIME)
                .setIssuedAt(datePresent)
                .setExpiration(dateExpired)
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    public UserProfile toUserProfile(String token) throws
            MalformedJwtException,
            ExpiredJwtException,
            UnsupportedJwtException,
            IllegalArgumentException,
            SignatureException {
        String username = Jwts
                .parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        Optional<UserProfile> userProfileOptional = userProfileRepository.findFirstByUsername(username);
        return userProfileOptional.orElse(null);
    }
}
