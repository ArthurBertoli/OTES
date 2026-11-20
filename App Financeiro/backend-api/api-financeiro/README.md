# API Financeiro

Back-end em Java com Spring Boot, SQLite e API REST para o app Android.

## Requisitos

- JDK 21 ou mais recente (`JAVA_HOME` configurado ou `java` no PATH).
- Internet na primeira execução: o Maven Wrapper (`mvnw.cmd`) baixa o Maven e as dependências.

## Como executar

### Opção mais simples no Windows

1. Abra a pasta do projeto.
2. Dê dois cliques em `run-backend.bat`.
3. Aguarde o Spring Boot subir em `http://localhost:8080`.

### Pelo terminal

```powershell
.\mvnw.cmd spring-boot:run
```

### Testes

```powershell
.\mvnw.cmd test
```

Os testes rodam com o perfil `test` (`src/test/resources/application-test.properties`), que usa um banco separado em `target/banco_financeiro_test.db`. Assim, rodar os testes não altera o `banco_financeiro.db` do projeto.

## Banco de dados

O arquivo SQLite fica na raiz do projeto com o nome `banco_financeiro.db` e é criado automaticamente se não existir.

## Base da API

O Android deve apontar para `http://10.0.2.2:8080/api/` no emulador.

## Endpoints principais

- `POST /api/transacoes`
- `GET /api/transacoes` (filtros opcionais `tipo`, `categoria`, `mes` e `ano`; `mes` sem `ano` filtra aquele mês em qualquer ano)
- `DELETE /api/transacoes/{id}`
