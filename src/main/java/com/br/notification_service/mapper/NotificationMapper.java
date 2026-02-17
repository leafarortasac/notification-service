package com.br.notification_service.mapper;

import com.br.notification_service.document.NotificationDocument;
import com.br.shared.contracts.model.*;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = true)
)
public interface NotificationMapper {

    List<NotificationDocumentRepresentation> toListNotificationDocumentRepresentation(List<NotificationDocument> list);

    List<NotificationDocument> toListNotificationDocument(List<NotificationRepresentation> representations);

    @Mapping(target = "notification", source = "representation")
    NotificationDocument toDocument(NotificationRepresentation representation);
}