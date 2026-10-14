package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRestRequest(
    @Schema(example = "ana@gmail.com")
        @NotBlank(message = "email must not be blank")
        @Email(message = "email must be a valid email address")
        String email,
    @Schema(example = "Clave1234") @NotBlank(message = "password must not be blank")
        String password) {}
