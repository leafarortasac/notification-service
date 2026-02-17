Notification Service (Consumer & History) 🔔📂

O Notification Service é o componente responsável por garantir que as informações sobre o ciclo de vida das tarefas cheguem aos interessados finais. Ele atua como um Worker assíncrono, consumindo eventos do RabbitMQ, persistindo o histórico de notificações no MongoDB e disparando alertas em tempo real.

🎯 Responsabilidades
Mensageria (Consumer): Escuta a fila task.created.queue e processa os eventos disparados pelo Task-Service em tempo real.

Persistência de Histórico (NoSQL): Armazena cada notificação enviada no MongoDB para fins de auditoria, reenvio e histórico do usuário.

Disparo de Alertas: Integração preparada para repassar notificações via protocolos leves (como MQTT) ou Logs de sistema.

Segurança (IAM): Utiliza o IAM-Service para proteger os endpoints de consulta ao histórico de notificações via JWT.

Transformação de Dados: Converte os eventos recebidos (TaskRepresentation) em documentos de domínio para persistência.

🛠️ Tecnologias
Java 21 & Spring Boot 3.4: Base moderna e performática.

Spring Data MongoDB: Persistência de documentos de histórico de forma flexível.

Spring AMQP: Integração robusta para consumo de filas do RabbitMQ.

Spring Security & JWT: Proteção de endpoints de consulta.

Swagger/OpenAPI: Documentação interativa da API de consulta.

Lombok & MapStruct: Produtividade e mapeamento de objetos.

📖 Documentação da API (Swagger)
Embora sua função principal seja o consumo de mensagens, o serviço expõe endpoints para que o usuário ou o Front-end consulte seu histórico de notificações.
🔗 Acesse em: http://localhost:8082/swagger-ui.html

Nota de Segurança: Todas as rotas de consulta exigem um Token JWT válido no Header Authorization: Bearer <TOKEN>.

🔐 Configuração de Ambiente
O serviço utiliza as seguintes variáveis para garantir a conectividade no ecossistema:

IAM_JWT_SECRET: Segredo para validação de tokens nas rotas de consulta.

SPRING_DATA_MONGODB_URI: Conexão com o cluster MongoDB (notifications_db).

SPRING_RABBITMQ_HOST: Endereço do broker RabbitMQ (task-rabbit).

🔄 Fluxo de Processamento
O serviço permanece em listen na fila do RabbitMQ.

Assim que o Task-Service posta uma mensagem de criação/alteração, o Notification-Service a captura.

O payload é processado e transformado em um documento de Histórico de Notificação.

A notificação é persistida no MongoDB.

(Opcional) O evento é encaminhado para o Broker MQTT para atualização em tempo real no Front-end.

📦 Como rodar localmente
Certifique-se de que o Shared Contracts foi instalado localmente (mvn install).

Garanta que os containers do MongoDB e RabbitMQ estejam rodando via Docker Compose.

Execute o comando:

Bash
mvn spring-boot:run