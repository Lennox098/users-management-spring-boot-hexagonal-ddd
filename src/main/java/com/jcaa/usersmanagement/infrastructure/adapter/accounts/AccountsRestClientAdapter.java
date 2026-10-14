package com.jcaa.usersmanagement.infrastructure.adapter.accounts;

import com.jcaa.usersmanagement.application.port.out.CreateAccountPort;
import com.jcaa.usersmanagement.domain.exception.AccountCreationException;
import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.infrastructure.adapter.accounts.dto.CreateAccountRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

/** Paso 5 del diagrama: users le pide a accounts (sync) que cree la cuenta del usuario nuevo. */
@Slf4j
@Component
@RequiredArgsConstructor
public class AccountsRestClientAdapter implements CreateAccountPort {

  private static final String ACCOUNTS_PATH = "/api/accounts";
  private static final String LOG_CREATED =
      "[AccountsRestClientAdapter] cuenta solicitada correctamente. userId={}";

  private final RestClient accountsRestClient;

  @Override
  public void createAccountFor(final UserModel user) {
    final CreateAccountRequest request =
        new CreateAccountRequest(
            user.getId().value(), user.getEmail().value(), user.getName().value());
    try {
      accountsRestClient
          .post()
          .uri(ACCOUNTS_PATH)
          .contentType(MediaType.APPLICATION_JSON)
          .body(request)
          .retrieve()
          .toBodilessEntity();
      log.info(LOG_CREATED, user.getId().value());
    } catch (final RestClientException restClientException) {
      throw AccountCreationException.becauseAccountsServiceFailed(
          user.getId().value(), restClientException);
    }
  }
}
