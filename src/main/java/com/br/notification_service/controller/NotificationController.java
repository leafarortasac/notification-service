package com.br.notification_service.controller;

import com.br.notification_service.document.NotificationDocument;
import com.br.notification_service.mapper.NotificationMapper;
import com.br.notification_service.service.NotificationService;
import com.br.shared.contracts.api.NotificationsApi;
import com.br.shared.contracts.model.NotificationDocumentResponseRepresentation;
import com.br.shared.contracts.model.NotificationRepresentation;
import com.br.shared.contracts.model.PaginaRepresentation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Log4j2
public class NotificationController implements NotificationsApi {

    private final NotificationService service;

    private final NotificationMapper mapper;

    @Override
    public ResponseEntity<NotificationDocumentResponseRepresentation> listNotifications(
            final UUID taskId,
            final String usuarioId,
            final Boolean lida,
            final Integer limit,
            final Boolean unPaged) {

        Page<NotificationDocument> page = service.getNotifications(
                taskId, usuarioId, lida, limit, unPaged);

        List<NotificationDocument> registros = page.getContent();

        var paginaInfo = new PaginaRepresentation();
        paginaInfo.setTotalPaginas(page.getTotalPages());
        paginaInfo.setTotalElementos(page.getTotalElements());

        var response = new NotificationDocumentResponseRepresentation();
        response.setRegistros(mapper.toListNotificationDocumentRepresentation(registros));
        response.setPagina(paginaInfo);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> putNotifications(
            final List<NotificationRepresentation> list) {

        log.info("Recebendo solicitação para atualizar {} notificações", list.size());
        service.updateNotifications(mapper.toListNotificationDocument(list));

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
