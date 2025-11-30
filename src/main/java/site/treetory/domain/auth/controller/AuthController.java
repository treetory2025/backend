package site.treetory.domain.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.treetory.domain.auth.service.AuthService;
import site.treetory.global.dto.ResponseDto;

import static site.treetory.global.statuscode.SuccessCode.OK;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/reissue")
    public ResponseDto<Void> reissue(HttpServletRequest request,
                                     HttpServletResponse response) {

        authService.reissue(request, response);

        return ResponseDto.success(OK);
    }

    @PostMapping("/logout")
    public ResponseDto<Void> logout(HttpServletRequest request,
                                    HttpServletResponse response) {

        authService.logout(request, response);

        return ResponseDto.success(OK);
    }
}
