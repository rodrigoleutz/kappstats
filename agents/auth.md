# Fluxo de Autenticação e JWT

A autenticação no KAppStats é centralizada em torno de tokens JWT e é aplicada tanto em rotas HTTP
quanto em conexões WebSocket.

## 1. Armazenamento de Token

O cliente armazena o token JWT usando `AuthTokenRepository`. Este token é recuperado sempre que uma
nova conexão autenticada é necessária.

## 2. Autenticação HTTP

As rotas protegidas no `server` utilizam o plugin de autenticação do Ktor configurado para JWT.

- **Configuração**: Definida em `PresentationConstants.Auth.JWT`.
- **Uso**: `authenticate(PresentationConstants.Auth.JWT) { ... }`.

## 3. Autenticação via WebSocket

Existem três tipos de conexões WebSocket:

- **Pública**: `AppEndpoints.WebSocket.path` (Ações como Ping).
- **Autenticada**: `AppEndpoints.WebSocket.Auth.fullPath` (Ações de usuário e monitoramento).
- **Dashboard**: `AppEndpoints.WebSocket.Dashboard.fullPath` (Dados em tempo real do sistema).

### Processo de Conexão:

1. O cliente chama `AuthAuthenticateUseCase`.
2. O token é enviado no header `Authorization: Bearer <token>` durante o handshake do WebSocket.
3. O servidor valida o token e cria uma `WebSocketConnection` vinculada ao `profileId` do usuário.
4. O servidor armazena essa conexão em `WebSocketData` para gerenciar para quem as mensagens devem
   ser enviadas.

## 4. Segurança nas Actions

Cada `WsActionBase` possui uma propriedade `isAuth`. No servidor, `WebSocketActionsImpl` verifica se
a ação requer autenticação e se a conexão atual (`ConnectionInfo`) possui as credenciais necessárias
antes de processar o pedido.

```kotlin
if (action.base.isAuthAction && connectionInfo !is AuthConnectionInfo) return null
```

## 5. Renovação de Token

(Aguardando detalhes da implementação de Refresh Token se aplicável). Atualmente focado em
autenticação via JWT Bearer.
