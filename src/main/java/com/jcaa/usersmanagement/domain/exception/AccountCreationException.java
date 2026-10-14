package com.jcaa.usersmanagement.domain.exception;

public final class AccountCreationException extends DomainException {

  private static final String MESSAGE_FAILED =
      "No se pudo crear la cuenta del usuario con ID: '%s'.";

  private AccountCreationException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public static AccountCreationException becauseAccountsServiceFailed(
      final String userId, final Throwable cause) {
    return new AccountCreationException(String.format(MESSAGE_FAILED, userId), cause);
  }
}
