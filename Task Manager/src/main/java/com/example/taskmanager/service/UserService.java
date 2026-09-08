package com.example.taskmanager.service;

import com.example.taskmanager.model.PasswordResetToken;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.PasswordResetTokenRepository;
import com.example.taskmanager.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository resetTokenRepository;
    private final EmailService emailService;

    // Ek hi Constructor me saari dependencies inject hongi
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       PasswordResetTokenRepository resetTokenRepository,
                       EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.resetTokenRepository = resetTokenRepository;
        this.emailService = emailService;
    }

    public User register(String username, String email, String password) {

        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already taken");
        }

        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setName(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setAuthProvider("LOCAL");

        return userRepository.save(user);
    }

    public User login(String identifier, String rawPassword) {

        User user = userRepository.findByUsernameOrEmail(identifier, identifier)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (user.getPassword() == null) {
            throw new RuntimeException("This account uses Google Sign-In. Please continue with Google, or set a password from your profile.");
        }

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }

    public User loginOrRegisterWithGoogle(String email, String googleId, String name) {

        return userRepository.findByEmail(email).map(existingUser -> {

            if (existingUser.getGoogleId() == null) {
                existingUser.setGoogleId(googleId);
                if (existingUser.getName() == null) {
                    existingUser.setName(name);
                }
                userRepository.save(existingUser);
            }
            return existingUser;

        }).orElseGet(() -> {

            User newUser = new User();
            newUser.setEmail(email);
            newUser.setGoogleId(googleId);
            newUser.setAuthProvider("GOOGLE");
            newUser.setRole("ROLE_USER");
            newUser.setName(name);
            newUser.setUsername(email);

            return userRepository.save(newUser);
        });
    }

    @Transactional
    public void requestPasswordReset(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No account found with this email"));

        resetTokenRepository.deleteByUserId(user.getId());

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));

        resetTokenRepository.save(resetToken);

        emailService.sendPasswordResetEmail(user.getEmail(), token);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {

        PasswordResetToken resetToken = resetTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired reset link"));

        if (resetToken.isExpired()) {
            throw new RuntimeException("This reset link has expired. Please request a new one.");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetTokenRepository.deleteByUserId(user.getId());
    }

    public User loginOrRegisterWithPhone(String phoneNumber) {

        return userRepository.findByPhoneNumber(phoneNumber).orElseGet(() -> {

            User newUser = new User();
            newUser.setPhoneNumber(phoneNumber);
            newUser.setUsername(phoneNumber);
            newUser.setName(phoneNumber);
            newUser.setAuthProvider("PHONE");
            newUser.setRole("ROLE_USER");

            return userRepository.save(newUser);
        });
    }
}