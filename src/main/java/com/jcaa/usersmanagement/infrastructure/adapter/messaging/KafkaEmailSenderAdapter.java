package com.jcaa.usersmanagement.infrastructure.adapter.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcaa.usersmanagement.application.port.out.EmailSenderPort;
import com.jcaa.usersmanagement.domain.exception.EmailSenderException;
import com.jcaa.usersmanagement.domain.model.EmailDestinationModel;
import com.jcaa.usersmanagement.infrastructure.adapter.messaging.dto.NotificationMessage;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.KafkaException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Paso 2 del diagrama: en lugar de enviar el correo directamente, deja el mensaje en el tópico
 * de Kafka para que notifiers lo envíe (async: no espera a que el correo salga).
 */
@Slf4j
@Component
public class KafkaEmailSenderAdapter implements EmailSenderPort {

  private static final String PROP_TOPIC = "${kafka.topic}";
  private static final String LOG_PUBLISHED =
      "[KafkaEmailSenderAdapter] notificación publicada. topic={} partition={} offset={}";
  private static final String LOG_PUBLISH_FAILED =
      "[KafkaEmailSenderAdapter] no se pudo publicar la notificación. topic={} causa={}";

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;
  private final String topic;

  public KafkaEmailSenderAdapter(
      final KafkaTemplate<String, String> kafkaTemplate,
      final ObjectMapper objectMapper,
      @Value(PROP_TOPIC) final String topic) {
    this.kafkaTemplate = kafkaTemplate;
    this.objectMapper = objectMapper;
    this.topic = topic;
  }

  @Override
  public void send(final EmailDestinationModel destination) {
    final String payload = toJson(destination);
    try {
      kafkaTemplate
          .send(topic, payload)
          .whenComplete(
              (result, error) -> {
                if (Objects.nonNull(error)) {
                  log.error(LOG_PUBLISH_FAILED, topic, error.getMessage());
                  return;
                }
                log.info(
                    LOG_PUBLISHED,
                    topic,
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().offset());
              });
    } catch (final KafkaException kafkaException) {
      throw EmailSenderException.becauseSendFailed(kafkaException);
    }
  }

  private String toJson(final EmailDestinationModel destination) {
    final NotificationMessage message =
        new NotificationMessage(
            destination.getDestinationEmail(),
            destination.getDestinationName(),
            destination.getSubject(),
            destination.getBody());
    try {
      return objectMapper.writeValueAsString(message);
    } catch (final JsonProcessingException jsonException) {
      throw EmailSenderException.becauseSendFailed(jsonException);
    }
  }
}
