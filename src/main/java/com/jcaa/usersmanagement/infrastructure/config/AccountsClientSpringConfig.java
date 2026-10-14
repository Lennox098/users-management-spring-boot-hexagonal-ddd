package com.jcaa.usersmanagement.infrastructure.config;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration(proxyBeanMethods = false)
public class AccountsClientSpringConfig {

  private static final String PROP_ACCOUNTS_URL = "${accounts.url}";

  // Render (plan gratuito) puede tardar ~1 minuto en despertar el servicio accounts
  private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(10);
  private static final Duration READ_TIMEOUT = Duration.ofSeconds(90);

  @Bean
  public RestClient accountsRestClient(@Value(PROP_ACCOUNTS_URL) final String accountsUrl) {
    return buildAccountsRestClient(accountsUrl);
  }

  static RestClient buildAccountsRestClient(final String accountsUrl) {
    final SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
    requestFactory.setConnectTimeout(CONNECT_TIMEOUT);
    requestFactory.setReadTimeout(READ_TIMEOUT);
    return RestClient.builder().baseUrl(accountsUrl).requestFactory(requestFactory).build();
  }
}
