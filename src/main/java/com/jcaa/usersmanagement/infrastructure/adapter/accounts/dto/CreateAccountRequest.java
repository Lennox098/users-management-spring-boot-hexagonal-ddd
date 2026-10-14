package com.jcaa.usersmanagement.infrastructure.adapter.accounts.dto;

/** Cuerpo acordado por el grupo para POST /api/accounts del microservicio accounts. */
public record CreateAccountRequest(String userId, String email, String nombre) {}
