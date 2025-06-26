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
    
    // This class is used to handle authentication-related REST API requests
    @Autowired
    private UserLoginService userLoginService;

    // The @Controller annotation indicates that this class is a controller in the Spring MVC framework
    @PostMapping("/login")
    public ResponseEntity <AuthResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response){
        return userLoginService.login(response, loginRequest);
    }
    
    // The @PostMapping annotation maps HTTP POST requests onto specific handler methods
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(
            @CookieValue(name = "RefreshToken", required = false) String refreshToken, HttpServletResponse response) {

        return userLoginService.refresh(response, refreshToken);
    }

    // The @PostMapping annotation maps HTTP POST requests onto specific handler methods
    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logOut(HttpServletResponse response) {
        return ResponseEntity.ok(new AuthResponse(Status.SUCCESS, userLoginService.logout(response)));
    }
}
