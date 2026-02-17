package com.br.notification_service.service;

import com.br.notification_service.document.NotificationDocument;
import com.br.notification_service.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class NotificationService {

    private final NotificationRepository repository;

    public Page<NotificationDocument> getNotifications(
            final UUID taskId,
            final String usuarioId,
            final Boolean lida,
            final Integer limit,
            final Boolean unPaged) {


        return repository.findNotificationsCustom(
                taskId, usuarioId, lida, limit, unPaged);
    }

    public void updateNotifications(
            final List<NotificationDocument> notifications){

        notifications.forEach(notification -> {

            var existingNotification = repository.findById(notification.getId())
                    .orElseThrow(() -> new RuntimeException("Notificação não encontrada para o ID: " + notification.getId()));

            existingNotification.getNotification().setLida(notification.getNotification().getLida());

            repository.save(existingNotification);

        });
    }
}
