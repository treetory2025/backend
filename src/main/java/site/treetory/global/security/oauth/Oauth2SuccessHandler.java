package site.treetory.global.security.oauth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import site.treetory.global.security.jwt.JwtProperties;
import site.treetory.global.security.jwt.JwtUtils;
import site.treetory.global.util.CookieUtils;
import site.treetory.global.util.RedisUtils;

import java.io.IOException;
import java.util.UUID;

import static site.treetory.global.security.jwt.JwtConstants.ACCESS;
import static site.treetory.global.security.jwt.JwtConstants.REFRESH;

@Component
@RequiredArgsConstructor
public class Oauth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;
    private final JwtProperties jwtProperties;
    private final CookieUtils cookieUtils;
    private final RedisUtils redisUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String uuid = oAuth2User.getUuid();
        String jti = UUID.randomUUID().toString();

        String accessToken = jwtUtils.createAccessToken(uuid, jti);
        String refreshToken = jwtUtils.createRefreshToken(uuid, jti);

        Cookie accessCookie = cookieUtils.createCookie(ACCESS, accessToken, "/", jwtProperties.getAccessExpiration());
        Cookie refreshCookie = cookieUtils.createCookie(REFRESH, refreshToken, "/api/auth/", jwtProperties.getRefreshExpiration());

        redisUtils.setDataWithExpiration("refresh:" + uuid + ":" + jti, refreshToken, jwtProperties.getRefreshExpiration());

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

        response.sendRedirect("/login/success");
    }
}
