package com.example.taskmanager.controller;

import com.example.taskmanager.model.User;
import com.example.taskmanager.service.FirebasePhoneAuthService;
import com.example.taskmanager.service.GoogleAuthService;
import com.example.taskmanager.service.UserService;
import com.example.taskmanager.util.JwtUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final GoogleAuthService googleAuthService;
    private final FirebasePhoneAuthService firebasePhoneAuthService;

    public AuthController(UserService userService,
                          JwtUtil jwtUtil,
                          GoogleAuthService googleAuthService,
                          FirebasePhoneAuthService firebasePhoneAuthService){
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.googleAuthService = googleAuthService;
        this.firebasePhoneAuthService = firebasePhoneAuthService;
    }

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody Map<String, String> body){

        String username = body.get("username");
        String email = body.get("email");
        String password = body.get("password");

        if (username == null || email == null || password == null
                || username.isBlank() || email.isBlank() || password.isBlank()) {
            throw new RuntimeException("Username, email and password are required");
        }

        User user = userService.register(username, email, password);

        String token = jwtUtil.generateToken(user.getUsername());

        return Map.of(
                "token", token,
                "username", user.getUsername(),
                "name", user.getName() != null ? user.getName() : user.getUsername()
        );
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> body){

        String identifier = body.get("identifier"); // username ya email
        String password = body.get("password");

        if (identifier == null || password == null) {
            throw new RuntimeException("Username/email and password are required");
        }

        User user = userService.login(identifier, password);

        String token = jwtUtil.generateToken(user.getUsername());

        return Map.of(
                "token", token,
                "username", user.getUsername(),
                "name", user.getName() != null ? user.getName() : user.getUsername()
        );
    }

    @PostMapping("/google")
    public Map<String, String> googleAuth(@RequestBody Map<String, String> body) {

        String idTokenString = body.get("credential");

        if (idTokenString == null || idTokenString.isBlank()) {
            throw new RuntimeException("Missing Google credential");
        }

        GoogleIdToken.Payload payload = googleAuthService.verifyToken(idTokenString);

        String email = payload.getEmail();
        String googleId = payload.getSubject();
        String name = (String) payload.get("name");

        User user = userService.loginOrRegisterWithGoogle(email, googleId, name);

        String token = jwtUtil.generateToken(user.getUsername());

        return Map.of(
                "token", token,
                "username", user.getUsername(),
                "name", user.getName() != null ? user.getName() : user.getUsername()
        );
    }

    @PostMapping("/phone")
    public Map<String, String> phoneAuth(@RequestBody Map<String, String> body) {

        String idToken = body.get("idToken");

        if (idToken == null || idToken.isBlank()) {
            throw new RuntimeException("Missing phone verification token");
        }

        FirebaseToken decodedToken = firebasePhoneAuthService.verifyToken(idToken);
        String phoneNumber = decodedToken.getClaims().get("phone_number").toString();

        User user = userService.loginOrRegisterWithPhone(phoneNumber);

        String token = jwtUtil.generateToken(user.getUsername());

        return Map.of(
                "token", token,
                "username", user.getUsername(),
                "name", user.getName() != null ? user.getName() : user.getUsername()
        );
    }

    @PostMapping("/forgot-password")
    public Map<String, String> forgotPassword(@RequestBody Map<String, String> body) {

        String email = body.get("email");
        if (email == null || email.isBlank()) {
            throw new RuntimeException("Email is required");
        }

        userService.requestPasswordReset(email);

        return Map.of("message", "Password reset link sent to your email");
    }

    @PostMapping("/reset-password")
    public Map<String, String> resetPassword(@RequestBody Map<String, String> body) {

        String token = body.get("token");
        String newPassword = body.get("newPassword");

        if (token == null || newPassword == null || newPassword.isBlank()) {
            throw new RuntimeException("Token and new password are required");
        }

        userService.resetPassword(token, newPassword);

        return Map.of("message", "Password reset successful. You can now log in.");
    }
}