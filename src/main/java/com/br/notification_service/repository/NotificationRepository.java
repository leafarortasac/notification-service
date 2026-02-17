package com.br.notification_service.repository;

import com.br.notification_service.document.NotificationDocument;
import com.br.notification_service.repository.custom.NotificationRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface NotificationRepository extends MongoRepository<NotificationDocument, String>, NotificationRepositoryCustom {

    @Query("{ 'order.id' : ?0, 'order.codfilial' : ?1 }")
    Optional<NotificationDocument> getByIdAndCodFilial(
            final String id,
            final String codfilial);

    boolean existsByNotificationTaskId(UUID taskId);
}
