package site.treetory.global.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
        Access access,
        Refresh refresh
) {

    public long getAccessExpiration() {
        return access.expiration();
    }

    public long getRefreshExpiration() {
        return refresh.expiration();
    }

    public String getAccessSecret() {
        return access.secret();
    }

    public String getRefreshSecret() {
        return refresh.secret();
    }

    public record Access(String secret, long expiration) {}
    public record Refresh(String secret, long expiration) {}
}
