package com.jcaa.usersmanagement.infrastructure.adapter.persistence.config;

public record DatabaseConfig(
    String host, int port, String databaseName, String username, String password, String sslMode) {
  private static final String URL_TEMPLATE =
      "jdbc:mysql://%s:%d/%s?sslMode=%s&serverTimezone=UTC&allowPublicKeyRetrieval=true";

  // MySQL local no usa TLS; TiDB Cloud exige conexión cifrada (sslMode=REQUIRED o VERIFY_IDENTITY)
  private static final String DEFAULT_SSL_MODE = "DISABLED";

  public DatabaseConfig(
      final String host,
      final int port,
      final String databaseName,
      final String username,
      final String password) {
    this(host, port, databaseName, username, password, DEFAULT_SSL_MODE);
  }

  public String buildJdbcUrl() {
    return String.format(URL_TEMPLATE, host, port, databaseName, sslMode);
  }
}
