# Serviço Auditor DLQ

## Sobre o projeto

Serviço responsável por consumir mensagens de uma fila DLQ (Dead Letter Queue) da AWS SQS e salvar os erros em um banco de auditoria para análise posterior.

O sistema:

- Escuta continuamente a fila DLQ
- Consome mensagens com falha
- Calcula a severidade do erro
- Persiste os dados no banco H2
- Remove a mensagem da fila apenas após salvar no banco

---

# Arquitetura escolhida

Foi utilizada **Layered Architecture (Arquitetura em Camadas)**.

A escolha foi feita porque o projeto possui responsabilidades bem separadas e não necessita de arquiteturas mais complexas.

Essa abordagem facilita:

- Organização do código
- Separação de responsabilidades
- Manutenção
- Escalabilidade futura
- Facilidade de entendimento

---

# Estrutura do projeto

```text
src/main/java/com/servico_auditor_dlq

├── adapter
├── config
├── dto
├── entity
├── exception
├── port
├── repository
└── service
```

---

# Justificativa da organização

## Adapter

Responsável pela comunicação com a AWS SQS.

O `SqsDlqAdapter` escuta a fila utilizando `@SqsListener`, evitando misturar infraestrutura com regra de negócio.

---

## DTO

Os DTOs representam os dados recebidos da fila.

Isso evita acoplamento entre a mensagem externa e a entidade do banco.

---

## Service

A camada service centraliza as regras de negócio.

Responsável por:

- Calcular severidade
- Converter payload
- Criar entidade
- Salvar no banco

### Regra de severidade

| Quantidade de itens | Severidade |
|---|---|
| > 100 | HIGH |
| 50 até 100 | MEDIUM |
| < 50 | LOW |

---

## Repository

Responsável pela persistência utilizando Spring Data JPA.

---

## Entity

Representa a tabela de auditoria persistida no banco.

Campos:

```json
{
  "errorId": "uuid",
  "queueName": "nome_fila",
  "payload": "mensagem",
  "timestamp": "data",
  "status": "PENDING_ANALYSIS",
  "severity": "HIGH"
}
```

---

## Exception

Centraliza os erros internos da aplicação através da `AuditoriaException`.

---

## Config

Utilizado para configurações globais da aplicação.

O `JacksonConfig` foi criado para permitir suporte ao tipo `Instant` no JSON recebido pela SQS.

---

# Fluxo da aplicação

1. O Adapter escuta a fila DLQ
2. A mensagem chega da AWS SQS
3. O DTO recebe os dados
4. O Service processa a mensagem
5. A severidade é calculada
6. A entidade é criada
7. O Repository salva no banco
8. A mensagem é removida da fila

---

# Tecnologias utilizadas

- Java 21
- Spring Boot
- Spring Data JPA
- Spring Cloud AWS SQS
- H2 Database
- Lombok
- Jackson
- Maven
- AWS SQS

---

# Configuração AWS

```yaml
spring:
  cloud:
    aws:
      region:
        static: us-east-2

      credentials:
        access-key: SUA_ACCESS_KEY
        secret-key: SUA_SECRET_KEY
```

---

# Banco H2

```text
http://localhost:8081/h2-console
```

---

# Exemplo de mensagem recebida

```json
{
  "zipCode": "80010000",
  "customerId": 1,
  "orderItems": [
    {
      "sku": 1,
      "amount": 5
    },
    {
      "sku": 2,
      "amount": 3
    }
  ],
  "origin": "SQS_QUEUE",
  "occurredAt": "2024-05-20T14:30:00Z"
}
```

---

# Exemplo persistido

```json
{
  "errorId": "uuid-gerado",
  "queueName": "T03N_KAIKE_MIORANZA",
  "payload": "{ ... }",
  "timestamp": "2026-05-25T10:00:00Z",
  "status": "PENDING_ANALYSIS",
  "severity": "LOW"
}
```

---

# Conclusão

A arquitetura em camadas permitiu criar um serviço organizado, desacoplado e de fácil manutenção.

A separação das responsabilidades facilita futuras evoluções, como:

- PostgreSQL
- APIs REST
- Dashboard de auditoria
- Retry automático
- Monitoramento
- Observabilidade
