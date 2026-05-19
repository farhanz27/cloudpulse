package com.avantdream.cloudpulse.auth.dto;

import com.avantdream.cloudpulse.auth.entity.User;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String email,
        boolean active,
        Instant createdAt
) {
    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.isActive(), user.getCreatedAt());
    }
}
