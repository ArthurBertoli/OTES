# App Financeiro

> Projeto da disciplina **OTES** — Ciência da Computação, UDESC.

## Sobre

Aplicativo de controle financeiro pessoal dividido em duas partes: uma API REST em Java com Spring Boot, que guarda as transações em um banco SQLite, e um app Android que consome essa API. O usuário registra entradas e saídas, consulta o saldo e o histórico com filtros e pode agendar um lembrete diário para não esquecer de registrar as transações.

## Funcionalidades

**API (`backend-api`)**
- Cadastro de transações com descrição, valor, tipo (`ENTRADA` ou `SAIDA`), categoria e data, com validação dos campos.
- Listagem com filtros opcionais por tipo, categoria (busca parcial, sem diferenciar maiúsculas), mês e ano.
- Resumo junto da listagem: total de entradas, total de saídas e saldo.
- Exclusão de transações por id, com resposta 404 quando o id não existe.
- Respostas de erro padronizadas (status, mensagem e horário) para dados inválidos.

**App Android (`app-android`)**
- Tela inicial com saldo total, totais de entradas e saídas e atalhos para as demais telas.
- Tela de cadastro de transação, com a data de hoje preenchida automaticamente.
- Histórico em lista com filtros por tipo, categoria, mês e ano, valores coloridos por tipo e botão de excluir.
- Tela de lembretes: ativa uma notificação diária no horário escolhido (WorkManager), pedindo a permissão de notificação no Android 13 ou superior.

## Tecnologias

- **API:** Java 21, Spring Boot 3.5 (Web, Data JPA, Validation), SQLite (`sqlite-jdbc` + dialeto do Hibernate Community), Maven.
- **App:** Android (Java), Retrofit 2 + Gson, RecyclerView, WorkManager, Material Components, Gradle (Kotlin DSL); minSdk 24, targetSdk 36.

## Estrutura

```
App Financeiro/
├── backend-api/api-financeiro/
│   ├── src/main/java/com/financeiro/api/
│   │   ├── transacao/        # entidade, controller, service, repository, filtros (Specifications) e DTOs
│   │   └── shared/           # tratamento de exceções da API
│   ├── src/main/resources/application.properties   # configuração do SQLite e do JPA
│   ├── run-backend.bat       # atalho para subir a API no Windows
│   └── README.md             # instruções específicas da API
└── app-android/ProjetoOTES/
    └── app/src/main/
        ├── java/com/example/projetootes/
        │   ├── MainActivity, CadastroActivity, HistoricoActivity, NotificacoesActivity
        │   ├── ReminderWorker.java   # gera a notificação de lembrete
        │   ├── network/              # cliente Retrofit e interface da API
        │   ├── model/                # modelos de requisição e resposta
        │   └── ui/TransacaoAdapter.java  # adaptador da lista do histórico
        └── res/                      # layouts, strings (inclui a URL da API) e temas
```

Os arquivos `Transacao.kt`, `TransacaoAdapter.kt` e `TransacaoService.kt` na raiz do pacote do app são de uma versão inicial e não são usados pelas telas atuais.

## Como executar

1. **API:** dentro de `backend-api/api-financeiro`, rode `.\mvnw.cmd spring-boot:run` (ou dê dois cliques em `run-backend.bat`). Ela sobe em `http://localhost:8080` e cria o arquivo `banco_financeiro.db` na pasta do projeto.
2. **App:** abra `app-android/ProjetoOTES` no Android Studio e execute em um emulador. O app acessa a API em `http://10.0.2.2:8080/`, que é o endereço do computador visto pelo emulador. Para usar um celular físico, altere `api_base_url` em `app/src/main/res/values/strings.xml` para o IP da máquina que roda a API.

### Endpoints

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/api/transacoes` | Cria uma transação |
| `GET` | `/api/transacoes?tipo=&categoria=&mes=&ano=` | Lista as transações com o resumo (filtros opcionais) |
| `DELETE` | `/api/transacoes/{id}` | Exclui uma transação |

## Autores

Arthur Bertoli
