# Estrutura do Projeto KAppStats

O projeto segue uma arquitetura Kotlin Multiplatform (KMP) dividida em três pilares principais:

## 1. shared (:shared)

O módulo compartilhado é o núcleo do projeto. Ele contém:

- **Modelos de Dados**: Entidades usadas tanto no frontend quanto no backend.
- **DTOs**: Objetos de transferência de dados, incluindo as definições de WebSocket.
- **Endpoints**: `AppEndpoints.kt` define todas as rotas HTTP e WS de forma centralizada.
- **WebSocket Actions**: Localizado em `shared/src/commonMain/kotlin/com/kappstats/dto/web_socket/`.
  Aqui são definidas as constantes e estruturas de dados para cada ação que pode ser realizada via
  WebSocket.

## 2. server (:server)

Backend construído com **Ktor**.

- **Data Repository**: Acesso ao banco de dados (MongoDB/KMongo).
- **Domain Logic**: Implementação das `Actions` de WebSocket (ex: `AppsMonitorAddAction`).
- **Presentation**: Definição de rotas HTTP e configuração do servidor WebSocket em
  `WebSocketRoutes.kt`.

## 3. composeApp (:composeApp)

Frontend construído com **Compose Multiplatform**.

- **UI**: Telas e componentes compartilhados entre Android, Desktop e iOS.
- **Data Service**: `WebSocketService` gerencia a conexão e o envio de mensagens para o servidor.
- **Actions**: Implementação do lado do cliente para processar as respostas do servidor.

## Injeção de Dependência

O projeto utiliza **Koin** para injeção de dependência em todos os módulos.
