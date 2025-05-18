package es.dws.gym.gym.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import es.dws.gym.gym.security.jwt.AuthResponse;
import es.dws.gym.gym.security.jwt.AuthResponse.Status;
import es.dws.gym.gym.security.jwt.LoginRequest;
import es.dws.gym.gym.security.jwt.UserLoginService;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/api/auth/")
public class LoginRestControler {
    
    @Autowired
    private UserLoginService userLoginService;

    @PostMapping("/login")
    public ResponseEntity <AuthResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response){
        return userLoginService.login(response, loginRequest);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(
            @CookieValue(name = "RefreshToken", required = false) String refreshToken, HttpServletResponse response) {

        return userLoginService.refresh(response, refreshToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logOut(HttpServletResponse response) {
        return ResponseEntity.ok(new AuthResponse(Status.SUCCESS, userLoginService.logout(response)));
    }
}
