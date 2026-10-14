package com.jcaa.usersmanagement;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import com.jcaa.usersmanagement.application.port.out.CreateAccountPort;
import com.jcaa.usersmanagement.application.port.out.EmailSenderPort;
import com.jcaa.usersmanagement.infrastructure.adapter.accounts.AccountsRestClientAdapter;
import com.jcaa.usersmanagement.infrastructure.adapter.messaging.KafkaEmailSenderAdapter;
import javax.sql.DataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/** Verifica que el contexto de Spring arranque con Kafka y accounts conectados a los puertos. */
@SpringBootTest
@DisplayName("Contexto de Spring")
class MainContextTest {

  // Evita abrir una conexión real a MySQL/TiDB durante el test
  @MockBean private DataSource dataSource;

  @Autowired private EmailSenderPort emailSenderPort;
  @Autowired private CreateAccountPort createAccountPort;

  @Test
  @DisplayName("los puertos de salida usan los adaptadores de Kafka y de accounts")
  void shouldWireKafkaAndAccountsAdapters() {
    // Arrange — contexto levantado por @SpringBootTest

    // Act & Assert
    assertAll(
        () -> assertInstanceOf(KafkaEmailSenderAdapter.class, emailSenderPort),
        () -> assertInstanceOf(AccountsRestClientAdapter.class, createAccountPort));
  }
}
