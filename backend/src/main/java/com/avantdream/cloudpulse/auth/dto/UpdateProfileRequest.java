package com.avantdream.cloudpulse.auth.dto;

import jakarta.validation.constraints.Email;

public record UpdateProfileRequest(
        String name,
        @Email String email
) {}
