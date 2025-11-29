package site.treetory.domain.auth.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import site.treetory.global.exception.CustomException;
import site.treetory.global.security.jwt.JwtUtils;
import site.treetory.global.util.CookieUtils;
import site.treetory.global.util.RedisUtils;

import java.util.UUID;

import static site.treetory.global.security.jwt.JwtConstants.ACCESS;
import static site.treetory.global.security.jwt.JwtConstants.REFRESH;
import static site.treetory.global.statuscode.ErrorCode.INVALID_TOKEN;

@Service
@RequiredArgsConstructor
    public class AuthService {

    @Value("${jwt.access.expiration}")
    private long accessExpiration;

    @Value("${jwt.refresh.expiration}")
    private long refreshExpiration;

    private final JwtUtils jwtUtils;
    private final CookieUtils cookieUtils;
    private final RedisUtils redisUtils;

    public void reissue(HttpServletRequest request, HttpServletResponse response) {

        Cookie cookie = cookieUtils.getCookieFromRequest(request, REFRESH);

        if (cookie == null) {
            throw new CustomException(INVALID_TOKEN);
        }

        String refreshToken = cookie.getValue();

        Claims claims = jwtUtils.getClaims(refreshToken, REFRESH);
        String uuid = jwtUtils.getUuid(claims);
        String jti = jwtUtils.getJti(claims);

        String key = "refresh:" + uuid + ":" + jti;
        Object storedToken = redisUtils.getData(key);

        if (storedToken == null || !refreshToken.equals(storedToken.toString())) {
            throw new CustomException(INVALID_TOKEN);
        }

        redisUtils.deleteData(key);

        setNewToken(response, uuid);
    }

    private void setNewToken(HttpServletResponse response, String uuid) {

        String jti = UUID.randomUUID().toString();

        String accessToken = jwtUtils.createAccessToken(uuid, jti);
        String refreshToken = jwtUtils.createRefreshToken(uuid, jti);

        redisUtils.setDataWithExpiration("refresh:" + uuid + ":" + jti, refreshToken, refreshExpiration);

        Cookie accessCookie = cookieUtils.createCookie(ACCESS, accessToken, "/", accessExpiration);
        Cookie refreshCookie = cookieUtils.createCookie(REFRESH, refreshToken, "/api/auth/", refreshExpiration);

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {

        expireCookies(response);

        Cookie cookie = cookieUtils.getCookieFromRequest(request, REFRESH);

        if (cookie == null) {
            return;
        }

        String refreshToken = cookie.getValue();

        Claims claims = jwtUtils.getClaims(refreshToken, REFRESH);
        String uuid = jwtUtils.getUuid(claims);
        String jti = jwtUtils.getJti(claims);

        String key = "refresh" + uuid + ":" + jti;
        Object storedToken = redisUtils.getData(key);

        if (storedToken == null) {
            return;
        }

        redisUtils.deleteData(key);
    }

    private void expireCookies(HttpServletResponse response) {

        Cookie accessCookie = cookieUtils.createCookie(ACCESS, null, "/", 0);
        Cookie refreshCookie = cookieUtils.createCookie(REFRESH, null, "/api/auth/", 0);

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
    }
}
