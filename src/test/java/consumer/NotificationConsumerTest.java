package consumer;

import com.br.notification_service.consumer.NotificationConsumer;
import com.br.notification_service.document.NotificationDocument;
import com.br.notification_service.gateway.MqttGateway;
import com.br.notification_service.repository.NotificationRepository;
import com.br.shared.contracts.model.NotificacaoStatusRepresentation;
import com.br.shared.contracts.model.OperacaoRepresentation;
import com.br.shared.contracts.model.TaskRepresentation;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationConsumerTest {

    @Mock private NotificationRepository repository;
    @Mock private MqttGateway mqttGateway;
    @Mock private ObjectMapper objectMapper;

    @InjectMocks private NotificationConsumer consumer;

    @Test
    @DisplayName("Deve processar nova tarefa, salvar no Mongo e enviar via MQTT")
    void deveProcessarNotificacaoComSucesso() throws Exception {

        String usuarioId = "user-123";
        UUID taskId = UUID.randomUUID();

        var task = new TaskRepresentation();
        task.setId(taskId);
        task.setUsuarioId(usuarioId);
        task.setTitulo("Testar Desafio");
        task.setOperacao(OperacaoRepresentation.INCLUSAO);

        when(repository.existsByNotificationTaskId(taskId)).thenReturn(false);
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"json\":\"dummy\"}");

        consumer.consume(task);

        verify(repository, times(1)).save(any(NotificationDocument.class));

        verify(repository).save(argThat(doc ->
                doc.getNotification().getMensagem().equals("Uma nova tarefa foi criada") &&
                        doc.getNotification().getStatus() == NotificacaoStatusRepresentation.INCLUSAO
        ));
        
        String expectedTopic = "notificacoes/usuario/" + usuarioId;
        verify(mqttGateway, times(1)).sendToMqtt(eq("{\"json\":\"dummy\"}"), eq(expectedTopic));
    }
}