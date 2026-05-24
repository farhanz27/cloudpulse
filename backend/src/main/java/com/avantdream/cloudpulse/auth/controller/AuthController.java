package com.avantdream.cloudpulse.auth.controller;

import java.util.UUID;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.avantdream.cloudpulse.auth.dto.*;
import com.avantdream.cloudpulse.auth.entity.User;
import com.avantdream.cloudpulse.auth.entity.UserPreferences;
import com.avantdream.cloudpulse.auth.repository.UserPreferencesRepository;
import com.avantdream.cloudpulse.auth.repository.UserRepository;
import com.avantdream.cloudpulse.shared.config.AppProperties;
import com.avantdream.cloudpulse.shared.exception.BadRequestException;
import com.avantdream.cloudpulse.shared.exception.ConflictException;
import com.avantdream.cloudpulse.shared.exception.ResourceNotFoundException;
import com.avantdream.cloudpulse.shared.security.JwtService;
import com.avantdream.cloudpulse.shared.security.UserPrincipal;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final String COOKIE_NAME = "access_token";

    private final UserRepository userRepository;
    private final UserPreferencesRepository preferencesRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AppProperties props;

    public AuthController(UserRepository userRepository,
                          UserPreferencesRepository preferencesRepository,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService,
                          AppProperties props) {
        this.userRepository = userRepository;
        this.preferencesRepository = preferencesRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.props = props;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody RegisterRequest req, HttpServletResponse response) {
        if (userRepository.existsByEmail(req.email())) {
            throw new ConflictException("Email already registered");
        }
        User user = new User();
        user.setName(req.name());
        user.setEmail(req.email());
        user.setHashedPassword(passwordEncoder.encode(req.password()));
        user = userRepository.save(user);
        setAuthCookie(response, jwtService.generateToken(user.getId()));
        return UserResponse.from(user);
    }

    @PostMapping("/login")
    public UserResponse login(@Valid @RequestBody LoginRequest req, HttpServletResponse response) {
        User user = userRepository.findByEmail(req.email())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid email or password"));
        if (!passwordEncoder.matches(req.password(), user.getHashedPassword())) {
            throw new ResourceNotFoundException("Invalid email or password");
        }
        setAuthCookie(response, jwtService.generateToken(user.getId()));
        return UserResponse.from(user);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletResponse response) {
        clearAuthCookie(response);
    }

    @GetMapping("/me")
    public UserResponse me(@AuthenticationPrincipal UserPrincipal principal) {
        return UserResponse.from(principal.getUser());
    }

    @PatchMapping("/me")
    public UserResponse updateMe(@Valid @RequestBody UpdateProfileRequest req,
                                  @AuthenticationPrincipal UserPrincipal principal) {
        User user = principal.getUser();
        if (req.email() != null && !req.email().equals(user.getEmail())) {
            if (userRepository.existsByEmail(req.email())) {
                throw new ConflictException("Email already in use");
            }
            user.setEmail(req.email());
        }
        if (req.name() != null) {
            user.setName(req.name());
        }
        return UserResponse.from(userRepository.save(user));
    }

    @PatchMapping("/me/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@Valid @RequestBody ChangePasswordRequest req,
                               @AuthenticationPrincipal UserPrincipal principal) {
        User user = principal.getUser();
        if (!passwordEncoder.matches(req.currentPassword(), user.getHashedPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }
        user.setHashedPassword(passwordEncoder.encode(req.newPassword()));
        userRepository.save(user);
    }

    @GetMapping("/me/preferences")
    public PreferencesResponse getPreferences(@AuthenticationPrincipal UserPrincipal principal) {
        return preferencesRepository.findById(principal.getUser().getId())
                .map(PreferencesResponse::from)
                .orElse(PreferencesResponse.defaults());
    }

    @PatchMapping("/me/preferences")
    public PreferencesResponse updatePreferences(@RequestBody UpdatePreferencesRequest req,
                                                  @AuthenticationPrincipal UserPrincipal principal) {
        UUID userId = principal.getUser().getId();
        UserPreferences prefs = preferencesRepository.findById(userId).orElseGet(() -> {
            UserPreferences p = new UserPreferences();
            p.setUserId(userId);
            return p;
        });
        if (req.emailAlerts() != null) prefs.setEmailAlerts(req.emailAlerts());
        if (req.recoveryNotifications() != null) prefs.setRecoveryNotifications(req.recoveryNotifications());
        if (req.defaultIntervalSeconds() != null) prefs.setDefaultIntervalSeconds(req.defaultIntervalSeconds());
        if (req.defaultTimeoutSeconds() != null) prefs.setDefaultTimeoutSeconds(req.defaultTimeoutSeconds());
        if (req.defaultLatencyThresholdMs() != null) prefs.setDefaultLatencyThresholdMs(req.defaultLatencyThresholdMs());
        return PreferencesResponse.from(preferencesRepository.save(prefs));
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMe(@AuthenticationPrincipal UserPrincipal principal, HttpServletResponse response) {
        userRepository.delete(principal.getUser());
        clearAuthCookie(response);
    }

    private void setAuthCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(COOKIE_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(props.getJwt().getExpirationHours() * 3600);
        cookie.setPath("/");
        cookie.setAttribute("SameSite", "Lax");
        response.addCookie(cookie);
    }

    private void clearAuthCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(COOKIE_NAME, "");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setAttribute("SameSite", "Lax");
        response.addCookie(cookie);
    }
}
