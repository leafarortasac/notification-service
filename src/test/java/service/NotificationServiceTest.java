package service;

import com.br.notification_service.document.NotificationDocument;
import com.br.notification_service.repository.NotificationRepository;
import com.br.notification_service.service.NotificationService;
import com.br.shared.contracts.model.NotificationRepresentation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository repository;

    @InjectMocks
    private NotificationService service;

    @Test
    @DisplayName("Deve atualizar o status 'lida' das notificações com sucesso")
    void deveAtualizarStatusLida() {

        String id = "notif-123";

        NotificationRepresentation repOriginal = new NotificationRepresentation();
        repOriginal.setLida(false);
        NotificationDocument docOriginal = NotificationDocument.builder()
                .id(id).notification(repOriginal).build();

        NotificationRepresentation repUpdate = new NotificationRepresentation();
        repUpdate.setLida(true);
        NotificationDocument docUpdate = NotificationDocument.builder()
                .id(id).notification(repUpdate).build();

        when(repository.findById(id)).thenReturn(Optional.of(docOriginal));

        service.updateNotifications(List.of(docUpdate));

        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(argThat(doc ->
                doc.getNotification().getLida()
        ));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar notificação inexistente")
    void deveLancarExcecaoNoUpdate() {

        NotificationDocument doc = NotificationDocument.builder().id("invalid-id").build();
        when(repository.findById("invalid-id")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                service.updateNotifications(List.of(doc))
        );
        verify(repository, never()).save(any());
    }
}