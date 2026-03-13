# Documentação da API (Swagger/OpenAPI)

O KAppStats utiliza OpenAPI para documentar seus endpoints REST. A documentação é servida automaticamente pelo servidor Ktor.

## Como acessar
Com o servidor rodando, a documentação pode ser acessada em:
- **Swagger UI**: `http://localhost:8080/docs`
- **OpenAPI Spec (YAML)**: `http://localhost:8080/openapi`

## Arquivo de Configuração
A definição da API está localizada em:
`server/src/main/resources/openapi/documentation.yaml`

## Endpoints Documentados

### Usuário (`/v1/user`)
- `GET /authenticate`: Verifica validade do token JWT. (Requer Auth)
- `POST /sign_in`: Autenticação de usuário.
- `POST /sign_up`: Cadastro de novo usuário.
- `GET /has_username`: Verifica disponibilidade de username.

### Monitoramento (`/v1/app_monitor`)
- `GET /test`: Rota de teste de conectividade.

## Como atualizar a documentação
Ao adicionar uma nova rota HTTP no `server`:
1.  Defina a rota em `ApiRoutes.kt` ou suas sub-rotas.
2.  Atualize o arquivo `documentation.yaml` com o novo path, parâmetros e schemas necessários.
3.  Reinicie o servidor para visualizar as alterações no `/docs`.
