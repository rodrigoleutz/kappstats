# Comunicação via WebSocket e Actions

A comunicação em tempo real no KAppStats é baseada em uma arquitetura de `Actions` que permite um
contrato tipado entre o cliente (`composeApp`) e o servidor (`server`), utilizando o módulo `shared`
para as definições.

## 1. Definição no Shared (`:shared`)

As ações são definidas centralizadamente no arquivo `WebSocketEvents.kt`. Elas utilizam a classe
`WsActionBase<T, R>`, onde `T` é o tipo de entrada (Request) e `R` é o tipo de saída (Response).

### Exemplo de Definição:

```kotlin
object AppsMonitor : WsActionBase<Any?, Any?>(Authenticate, "/apps_monitor") {
    object Add : WsActionBase<AppMonitor, AppMonitor?>(
        parent = AppsMonitor,
        command = "/add",
        inputSerializer = AppMonitor.serializer(),
        outputSerializer = AppMonitor.serializer().nullable,
        isAuth = true
    )
}
```

- **`action`**: A string de rota final (ex: `/web_socket/auth/apps_monitor/add`).
- **`isAuth`**: Define se a ação requer um usuário autenticado.

## 2. Implementação no Servidor (`:server`)

O servidor implementa o contrato `WebSocketContract<T, R>` para cada ação.

### Fluxo no Servidor:

1. **`WebSocketRoutes.kt`**: Escuta as conexões Ktor WebSocket (Auth, Dashboard ou Public).
2. **`WebSocketEventBus`**: Recebe o `WebSocketRequest`, identifica a ação via `WebSocketActions` e
   processa os dados.
3. **Processamento**: O método `process` da Action executa a lógica de negócio e retorna um
   `WebSocketResponse`.

```kotlin
@WsAction
object AppsMonitorAddAction : WebSocketContract<AppMonitor, AppMonitor?> {
    override val base = WebSocketEvents.Authenticate.AppsMonitor.Add
    override suspend fun WebSocketRequest.process(...) {
        ...
    }
}
```

## 3. Implementação no Cliente (`:composeApp`)

O cliente utiliza `WebSocketService` para manter a conexão ativa e `WebSocketActions` para enviar e
receber mensagens.

### Fluxo no Cliente:

1. **`WebSocketService`**: Gerencia a `WebSocketSession` do Ktor Client.
2. **`WebSocketActionsImpl`**:
    - Conecta e escuta o fluxo `incoming`.
    - Decodifica `WebSocketResponse`.
    - Encaminha para o `receive` da Action correspondente.
3. **KSP (Kotlin Symbol Processing)**: O projeto utiliza KSP para gerar automaticamente a lista de
   Actions disponíveis via `WsActionsGenerated`.

## 4. Estrutura de Mensagens

- **`WebSocketRequest`**: Contém `id`, `action` (string da rota) e `data` (JSON string).
- **`WebSocketResponse`**: Pode ser `Success` ou `Failure`. Contém o `id` da requisição original
  para permitir o mapeamento de resposta no cliente.

## 5. Como adicionar uma nova Action

1. **Shared**: Adicione o objeto em `WebSocketEvents.kt`.
2. **Server**: Crie um novo objeto em `domain/web_socket/action/` implementando `WebSocketContract`.
   Anote com `@WsAction`.
3. **ComposeApp**: Crie a implementação correspondente que herda de `WebSocketContract` para lidar
   com a UI. Anote com `@WsAction`.
4. **Build**: Execute o build para que o KSP gere as classes `WsActionsGenerated`.
