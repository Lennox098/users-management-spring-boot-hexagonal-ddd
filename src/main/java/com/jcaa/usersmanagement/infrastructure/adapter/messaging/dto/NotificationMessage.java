package com.jcaa.usersmanagement.infrastructure.adapter.messaging.dto;

/**
 * Mensaje acordado por el grupo para el tópico "notificaciones".
 * users y accounts lo escriben; notifiers lo lee.
 */
public record NotificationMessage(String email, String nombre, String asunto, String mensaje) {}
