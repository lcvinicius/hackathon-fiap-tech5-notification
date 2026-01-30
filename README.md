# notification-service (MVP)

Serviço Java puro (Java 21) executado como AWS Lambda (handler), que:

1) Consome eventos do **SQS** (via `SQSEvent` no Lambda)
2) Consulta o **Postgres** em modo **somente leitura**
3) Gera uma mensagem simples de notificação
4) Publica via **AWS SNS (SMS)**
5) Registra logs do processamento

## O que este serviço NÃO faz

- Não cria infraestrutura AWS (SQS/SNS/Lambda/EventBridge)
- Não expõe API HTTP
- Não cadastra/atualiza/deleta dados no banco
- Não contém regras de negócio de medicamentos ou de inscrição

## Estrutura (responsabilidades)

- Handler de entrada: `com.notification.handler.NotificationLambdaHandler`
- Parser do evento: `com.notification.parser.EventParser`
- Consulta read-only: `com.notification.repository.JdbcSubscriberRepository`
- Processamento: `com.notification.service.NotificationProcessor`
- Publicação SNS: `com.notification.service.SnsSmsPublisher`

## Formato do evento

O body da mensagem SQS deve ser um JSON contendo pelo menos:

- `idMedicamento` (ou `medicineId`)
- `idPosto` (ou `ubsId`)

Campos opcionais (para melhorar a mensagem):

- `nomeMedicamento` (ou `medicineName`)
- `nomePosto` (ou `ubsName`)

Exemplo:

```json
{
  "idMedicamento": "med-123",
  "nomeMedicamento": "Dipirona",
  "idPosto": "ubs-456",
  "nomePosto": "UBS Centro"
}
```

## Variáveis de ambiente

### Banco (Postgres)

- `DB_JDBC_URL` (opcional) ex: `jdbc:postgresql://localhost:5432/notification`
- ou `DB_HOST`, `DB_PORT`, `DB_NAME`
- `DB_USER` (default: `postgres`)
- `DB_PASSWORD` (default: `postgres`)

### SNS

- `SNS_DISABLED` (`true`/`false`) — default recomendado local: `true`
- `SNS_PUBLISH_TARGET` (`phone` | `topic`) — default: `phone`
- Se `SNS_PUBLISH_TARGET=topic`: `SNS_TOPIC_ARN` obrigatório

Observação: para cumprir o “1 envio por usuário”, o modo recomendado é `SNS_PUBLISH_TARGET=phone`.

## Rodando local com Docker (recomendado)

Pré-requisito: Docker Desktop.

### TL;DR (teste MVP em 3 comandos)

Esse é o jeito mais simples de provar o fluxo completo (sem AWS):

1) Subir Postgres (com seed de dados)
2) Simular evento do SQS chamando o handler
3) Ver logs (evento lido, DB consultado, mensagem gerada, “envio” SNS mock)

**Windows / PowerShell (recomendado):**

1) Suba o banco:

```powershell
docker compose up -d db
```

2) Rode o serviço em modo `handler` (simula SQS -> Lambda) com SNS mock:

```powershell
docker compose run --rm `
  -e LOCAL_MODE=handler `
  -e RUN_DB_TEST=true `
  -e SNS_DISABLED=true `
  -e EVENT_JSON='{"idMedicamento":"med-123","idPosto":"ubs-456","nomeMedicamento":"Dipirona","nomePosto":"UBS Centro"}' `
  app
```

3) Pare tudo:

```powershell
docker compose down
```

**O que você precisa ver nos logs (as 4 provas do MVP):**

- `processing record` / `parsed event`  → leu o evento
- `subscribers found; count=2`          → consultou o banco
- `message=Seu medicamento Dipirona...`→ gerou a mensagem
- `[MOCK] Enviando SMS para ...`        → tentou publicar (SNS mock)

Se isso aparecer, o serviço está validado.

### Alternativa: subir tudo em foreground

Se preferir rodar tudo de uma vez (o app vai executar e sair):

```bash
docker compose up --build
```

O compose já cria um schema mínimo e dados de exemplo em `docker/db/init.sql`.

- O app roda com `SNS_DISABLED=true` (não chama AWS)
- Ele faz parse do `EVENT_JSON`, consulta inscritos e executa o fluxo completo.

Para parar:

```bash
docker compose down
```

## Rodando local sem Docker

1) Build (Java 21 + Maven):

```bash
mvn -DskipTests package
```

2) Executar (LocalRunner):

```bash
java -DSNS_DISABLED=true -jar target/notification-service-0.1.0-SNAPSHOT.jar
```

Para testar com DB, informe o banco e ligue o flag:

- `RUN_DB_TEST=true`
- `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`

## Como testar (MVP) — checklist oficial

Você só precisa provar 4 coisas pelos logs:

1) O serviço lê um evento
2) Consulta o banco corretamente (read-only)
3) Gera a mensagem certa
4) Tenta publicar no SNS (mock/local)

### Teste principal (vídeo/banca): simular evento SQS e chamar o handler

Use o TL;DR acima. Ele é o cenário ideal: sem AWS, sem SQS real, com banco real.

### Teste de integração (banco real) com seed

O `docker/db/init.sql` cria tabelas e insere 2 usuários inscritos em `med-123` + `ubs-456`.

Se você usar esse mesmo `EVENT_JSON`, a execução deve mostrar `count=2` e 2 linhas de envio mock.

## Lambda

O handler do Lambda é:

- `com.notification.handler.NotificationLambdaHandler`

O empacotamento é um jar único (shade). Em runtime Lambda, o evento chega como `SQSEvent` e cada record é processado isoladamente.
