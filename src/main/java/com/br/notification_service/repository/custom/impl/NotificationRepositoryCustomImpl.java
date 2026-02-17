package com.br.notification_service.repository.custom.impl;

import com.br.notification_service.document.NotificationDocument;
import com.br.notification_service.repository.custom.NotificationRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class NotificationRepositoryCustomImpl implements NotificationRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Page<NotificationDocument> findNotificationsCustom(
            final UUID taskId,
            final String usuarioId,
            final Boolean lida,
            final Integer limit,
            final Boolean unPaged) {

        Query query = new Query();
        List<Criteria> criteriaList = new ArrayList<>();

        if (taskId != null) criteriaList.add(Criteria.where("taskId").is(taskId));
        if (usuarioId != null) criteriaList.add(Criteria.where("usuarioId").is(usuarioId));

        criteriaList.add(Criteria.where("lida").is(lida));

        query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));

        Sort sort = Sort.by(Sort.Direction.ASC, "dataNotificacao");

        Pageable pageable = unPaged ? Pageable.unpaged() : PageRequest.of(0, limit, sort);

        query.with(sort);

        if (pageable.isPaged()) {
            query.with(pageable);
        }

        List<NotificationDocument> list = mongoTemplate.find(query, NotificationDocument.class);

        return PageableExecutionUtils.getPage(
                list,
                pageable,
                () -> mongoTemplate.count(query.skip(-1).limit(-1), NotificationDocument.class));
    }
}