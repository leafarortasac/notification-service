package com.br.notification_service.consumer;

import com.br.notification_service.config.RabbitMQConfig;
import com.br.notification_service.document.NotificationDocument;
import com.br.notification_service.gateway.MqttGateway;
import com.br.notification_service.repository.NotificationRepository;
import com.br.shared.contracts.model.NotificacaoStatusRepresentation;
import com.br.shared.contracts.model.NotificationRepresentation;
import com.br.shared.contracts.model.OperacaoRepresentation;
import com.br.shared.contracts.model.TaskRepresentation;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationRepository repository;
    private final MqttGateway mqttGateway;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void consume(TaskRepresentation task) {
        log.info("Recebida nova tarefa: {} para o usuário: {}", task.getId(), task.getUsuarioId());

        try {

            boolean exists = repository.existsByNotificationTaskId(task.getId());

            String mensagem = MENSAGENS_OPERACAO.getOrDefault(task.getOperacao(), "Atualização de tarefa");

            var notification = new NotificationRepresentation()
                    .id(UUID.randomUUID().toString())
                    .taskId(task.getId())
                    .usuarioId(task.getUsuarioId())
                    .titulo(task.getTitulo())
                    .mensagem(mensagem)
                    .lida(false)
                    .status(exists ? NotificacaoStatusRepresentation.ALTERACAO : NotificacaoStatusRepresentation.INCLUSAO )
                    .dataNotificacao(LocalDateTime.now());

            var document = NotificationDocument.builder()
                    .notification(notification)
                    .build();

            repository.save(document);

            log.info("Notificação gravada no MongoDB para a task {}", task.getId());

            String payload = objectMapper.writeValueAsString(notification);

            String topic = "notificacoes/usuario/" + task.getUsuarioId();

            mqttGateway.sendToMqtt(payload, topic);

            log.info("Notificação enviada via MQTT para o tópico: {}", topic);

        } catch (Exception e) {
            log.error("Erro ao processar notificação da task {}: {}", task.getId(), e.getMessage());
        }
    }

    private static final Map<OperacaoRepresentation, String> MENSAGENS_OPERACAO = Map.of(
            OperacaoRepresentation.INCLUSAO, "Uma nova tarefa foi criada",
            OperacaoRepresentation.ALTERACAO, "A tarefa foi alterada"
    );
}