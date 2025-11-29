package site.treetory.global.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import site.treetory.global.exception.CustomException;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static site.treetory.global.security.jwt.JwtConstants.*;
import static site.treetory.global.statuscode.ErrorCode.INVALID_TOKEN;

@Component
public class JwtUtils {

    private JwtProperties jwtProperties;

    private Key accessKey;
    private Key refreshKey;

    @PostConstruct
    protected void init() {
        this.accessKey = new SecretKeySpec(Base64.getDecoder().decode(jwtProperties.getAccessSecret()),
                HS256.getJcaName());
        this.refreshKey = new SecretKeySpec(Base64.getDecoder().decode(jwtProperties.getRefreshSecret()),
                HS256.getJcaName());
    }

    public String createAccessToken(String uuid, String jti) {

        Map<String, Object> claims = new HashMap<>();
        claims.put(CATEGORY_CLAIM_NAME, ACCESS);

        return createToken(uuid, jti, Jwts.claims(claims), jwtProperties.getAccessExpiration(), accessKey);
    }

    public String createRefreshToken(String uuid, String jti) {

        Map<String, Object> claims = new HashMap<>();
        claims.put(CATEGORY_CLAIM_NAME, REFRESH);

        return createToken(uuid, jti, Jwts.claims(claims), jwtProperties.getRefreshExpiration(), refreshKey);
    }

    private String createToken(String uuid, String jti, Claims claims, long expirationTime, Key key) {

        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(uuid)
                .setExpiration(expiration)
                .setIssuedAt(now)
                .setIssuer(ISSUER)
                .setId(jti)
                .signWith(key, HS256)
                .compact();
    }

    public Claims getClaims(String token, String expectedCategory) {

        try {
            Key key = expectedCategory.equals(ACCESS) ? accessKey : refreshKey;

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            if (!ISSUER.equals(claims.getIssuer())) {
                throw new CustomException(INVALID_TOKEN);
            }

            String category = claims.get(CATEGORY_CLAIM_NAME, String.class);

            if (!expectedCategory.equals(category)) {
                throw new CustomException(INVALID_TOKEN);
            }

            return claims;

        } catch (Exception e) {
            throw new CustomException(INVALID_TOKEN);
        }
    }

    public String getUuid(Claims claims) {

        return claims.getSubject();
    }

    public String getJti(Claims claims) {

        return claims.getId();
    }
}
