# KAppStats - Agent Instructions

Este documento serve como guia para agentes de IA que trabalham no projeto KAppStats. Ele descreve a
arquitetura do projeto e como os diferentes componentes se comunicam.

## Visão Geral do Projeto

O KAppStats é um projeto Kotlin Multiplatform (KMP) que utiliza Compose Multiplatform para o
frontend e Ktor para o backend.

## Módulos

- **[:shared](agents/project_structure.md#shared)**: Contém lógica de negócios, DTOs e definições de
  WebSocket compartilhadas entre o cliente e o servidor.
- **[:server](agents/project_structure.md#server)**: Backend Ktor que gerencia autenticação, banco
  de dados e comunicação WebSocket.
- **[:composeApp](agents/project_structure.md#composeapp)**: Frontend Compose Multiplatform (
  Android, Desktop, iOS).

## Instruções Específicas

Para entender partes específicas do sistema, consulte os seguintes guias:

1. **[Estrutura do Projeto](agents/project_structure.md)**: Detalhes sobre a organização dos
   módulos.
2. **[WebSocket e Actions](agents/websocket.md)**: Guia detalhado sobre como funciona a comunicação
   em tempo real e a definição de `Actions` no módulo `shared`.
3. **[Autenticação](agents/auth.md)**: Como o fluxo de JWT e autenticação via WebSocket está
   implementado.

## Como as Actions funcionam

As Actions são o coração da comunicação entre o app e o servidor. Elas são definidas no módulo
`shared` usando a classe `WsActionBase`.

Para adicionar uma nova funcionalidade via WebSocket:

1. Defina o objeto da Action em `WebSocketEvents.kt` no módulo `shared`.
2. Implemente o handler no `server`.
3. Implemente o consumo no `composeApp`.

Veja mais detalhes em [WebSocket e Actions](agents/websocket.md).
