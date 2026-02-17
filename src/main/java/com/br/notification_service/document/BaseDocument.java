package com.br.notification_service.document;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

@Data
@SuperBuilder
@NoArgsConstructor
public abstract class BaseDocument {

    @Id
    private String id;
}
