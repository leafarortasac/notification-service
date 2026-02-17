package com.br.notification_service.document;

import com.br.shared.contracts.model.NotificationRepresentation;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notifications")
@CompoundIndex(name = "idx_notification_idempotency",
        def = "{'notification.taskId': 1, 'notification.usuarioId': 1, 'notification.status': 1}")
@CompoundIndex(name = "idx_user_notifications_flow",
        def = "{'notification.usuarioId': 1, 'notification.lida': 1, 'notification.dataNotificacao': -1}")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class NotificationDocument extends BaseDocument {

    @org.springframework.data.mongodb.core.mapping.Unwrapped.Nullable
    private NotificationRepresentation notification;
}