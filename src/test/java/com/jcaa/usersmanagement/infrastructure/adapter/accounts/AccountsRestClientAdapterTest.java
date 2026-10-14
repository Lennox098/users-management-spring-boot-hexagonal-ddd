package com.jcaa.usersmanagement.infrastructure.adapter.accounts;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import com.jcaa.usersmanagement.domain.enums.UserRole;
import com.jcaa.usersmanagement.domain.enums.UserStatus;
import com.jcaa.usersmanagement.domain.exception.AccountCreationException;
import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.domain.valueobject.UserEmail;
import com.jcaa.usersmanagement.domain.valueobject.UserId;
import com.jcaa.usersmanagement.domain.valueobject.UserName;
import com.jcaa.usersmanagement.domain.valueobject.UserPassword;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

@DisplayName("AccountsRestClientAdapter")
class AccountsRestClientAdapterTest {

  private static final String BASE_URL = "http://accounts.test";
  private static final String EXPECTED_BODY =
      "{\"userId\":\"u-01\",\"email\":\"ana@gmail.com\",\"nombre\":\"Ana Perez\"}";

  private MockRestServiceServer server;
  private AccountsRestClientAdapter adapter;
  private UserModel user;

  @BeforeEach
  void setUp() {
    final RestClient.Builder builder = RestClient.builder().baseUrl(BASE_URL);
    server = MockRestServiceServer.bindTo(builder).build();
    adapter = new AccountsRestClientAdapter(builder.build());
    user =
        new UserModel(
            new UserId("u-01"),
            new UserName("Ana Perez"),
            new UserEmail("ana@gmail.com"),
            UserPassword.fromHash("$2a$12$hash"),
            UserRole.MEMBER,
            UserStatus.PENDING);
  }

  @Test
  @DisplayName("createAccountFor() hace POST /api/accounts con el cuerpo acordado")
  void shouldPostAgreedBodyToAccounts() {
    // Arrange
    server
        .expect(requestTo(BASE_URL + "/api/accounts"))
        .andExpect(method(HttpMethod.POST))
        .andExpect(content().json(EXPECTED_BODY, true))
        .andRespond(withStatus(HttpStatus.CREATED));

    // Act & Assert
    assertDoesNotThrow(() -> adapter.createAccountFor(user));
    server.verify();
  }

  @Test
  @DisplayName("createAccountFor() lanza AccountCreationException cuando accounts responde error")
  void shouldThrowWhenAccountsFails() {
    // Arrange
    server.expect(requestTo(BASE_URL + "/api/accounts")).andRespond(withServerError());

    // Act & Assert
    assertThrows(AccountCreationException.class, () -> adapter.createAccountFor(user));
  }
}
