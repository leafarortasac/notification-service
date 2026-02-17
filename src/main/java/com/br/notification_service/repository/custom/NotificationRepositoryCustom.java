package com.br.notification_service.repository.custom;

import com.br.notification_service.document.NotificationDocument;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface NotificationRepositoryCustom {

    Page<NotificationDocument> findNotificationsCustom(
            final UUID taskId,
            final String usuarioId,
            final Boolean lida,
            final Integer limit,
            final Boolean unPaged);
}
