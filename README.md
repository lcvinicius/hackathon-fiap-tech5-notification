# notification-service (MVP)

## O que este projeto faz

Serviço Java (Java 21) pensado para rodar como **AWS Lambda** que:

- Lê um evento do **SQS** (body JSON)
- Consulta no **Postgres** quais usuários estão inscritos (somente leitura)
- Gera a mensagem: `Seu medicamento X está disponível no posto Y.`
- “Envia” 1 SMS por usuário via **SNS** (no teste local fica mockado e só loga)

Não cria infraestrutura AWS e não expõe API HTTP.

## Como testar (rápido, sem AWS) — Docker

Pré-requisito: Docker Desktop.

1) Subir o Postgres (com seed):

```powershell
docker compose up -d db
```

2) Rodar o serviço simulando **SQS -> Lambda handler** com SNS mock:

```powershell
docker compose run --rm `
  -e LOCAL_MODE=handler `
  -e RUN_DB_TEST=true `
  -e SNS_DISABLED=true `
  -e EVENT_JSON='{"idMedicamento":"med-123","idPosto":"ubs-456","nomeMedicamento":"Dipirona","nomePosto":"UBS Centro"}' `
  app
```

3) Parar:

```powershell
docker compose down
```

### O que validar nos logs (4 provas do MVP)

- Leu o evento: `processing record` / `parsed event`
- Consultou o banco: `subscribers found; count=2`
- Gerou a mensagem: aparece em `message=...`
- Tentou enviar SNS (mock): `[MOCK] Enviando SMS para ...`
