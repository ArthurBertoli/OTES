# API Financeiro

Back-end em Java com Spring Boot, SQLite e API REST para o app Android.

## Como executar

### Opção mais simples no Windows

1. Abra a pasta do projeto.
2. Dê dois cliques em `run-backend.bat`.
3. Aguarde o Spring Boot subir em `http://localhost:8080`.

### Pelo terminal

```powershell
.\mvnw.cmd spring-boot:run
```

## Banco de dados

O arquivo SQLite é criado automaticamente na raiz do projeto com o nome `banco_financeiro.db`.

## Base da API

O Android deve apontar para `http://10.0.2.2:8080/api/` no emulador.

## Endpoints principais

- `POST /api/transacoes`
- `GET /api/transacoes`
- `DELETE /api/transacoes/{id}`