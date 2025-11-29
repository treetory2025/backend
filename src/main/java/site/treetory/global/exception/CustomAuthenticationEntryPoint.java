package site.treetory.global.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import site.treetory.global.dto.ResponseData;
import site.treetory.global.dto.ResponseHeader;

import java.io.IOException;

import static site.treetory.global.statuscode.ErrorCode.UNAUTHORIZED;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException{

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        ResponseData<Void> res = new ResponseData<>(new ResponseHeader(UNAUTHORIZED.getMessage()), null);
        response.getWriter().write(mapper.writeValueAsString(res));
    }
}
