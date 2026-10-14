package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jcaa.usersmanagement.application.port.in.CreateUserUseCase;
import com.jcaa.usersmanagement.application.port.in.DeleteUserUseCase;
import com.jcaa.usersmanagement.application.port.in.GetAllUsersUseCase;
import com.jcaa.usersmanagement.application.port.in.GetUserByIdUseCase;
import com.jcaa.usersmanagement.application.port.in.LoginUseCase;
import com.jcaa.usersmanagement.application.port.in.UpdateUserUseCase;
import com.jcaa.usersmanagement.domain.enums.UserRole;
import com.jcaa.usersmanagement.domain.enums.UserStatus;
import com.jcaa.usersmanagement.domain.exception.InvalidCredentialsException;
import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.domain.valueobject.UserEmail;
import com.jcaa.usersmanagement.domain.valueobject.UserId;
import com.jcaa.usersmanagement.domain.valueobject.UserName;
import com.jcaa.usersmanagement.domain.valueobject.UserPassword;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
    controllers = UserRestController.class,
    properties = "spring.main.web-application-type=servlet")
@DisplayName("UserRestController - POST /api/users/login")
class UserRestControllerLoginTest {

  private static final String LOGIN_BODY =
      "{\"email\":\"ana@gmail.com\",\"password\":\"Clave1234\"}";

  @Autowired private MockMvc mockMvc;

  @MockBean private CreateUserUseCase createUserUseCase;
  @MockBean private UpdateUserUseCase updateUserUseCase;
  @MockBean private DeleteUserUseCase deleteUserUseCase;
  @MockBean private GetUserByIdUseCase getUserByIdUseCase;
  @MockBean private GetAllUsersUseCase getAllUsersUseCase;
  @MockBean private LoginUseCase loginUseCase;

  @Test
  @DisplayName("responde 200 con los datos del usuario cuando las credenciales son válidas")
  void shouldReturnUserWhenCredentialsAreValid() throws Exception {
    // Arrange
    final UserModel user =
        new UserModel(
            new UserId("u-01"),
            new UserName("Ana Perez"),
            new UserEmail("ana@gmail.com"),
            UserPassword.fromHash("$2a$12$hash"),
            UserRole.MEMBER,
            UserStatus.ACTIVE);
    when(loginUseCase.execute(any())).thenReturn(user);

    // Act & Assert
    mockMvc
        .perform(post("/api/users/login").contentType(MediaType.APPLICATION_JSON).content(LOGIN_BODY))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value("u-01"))
        .andExpect(jsonPath("$.status").value("ACTIVE"));
  }

  @Test
  @DisplayName("responde 401 cuando las credenciales son inválidas")
  void shouldReturnUnauthorizedWhenCredentialsAreInvalid() throws Exception {
    // Arrange
    when(loginUseCase.execute(any()))
        .thenThrow(InvalidCredentialsException.becauseCredentialsAreInvalid());

    // Act & Assert
    mockMvc
        .perform(post("/api/users/login").contentType(MediaType.APPLICATION_JSON).content(LOGIN_BODY))
        .andExpect(status().isUnauthorized());
  }
}
