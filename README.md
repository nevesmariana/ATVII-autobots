# AutoManager - AutoBots

Sistema desenvolvido para as atividades práticas da disciplina, utilizando uma arquitetura baseada em Spring Boot (micro-serviço) para gerenciamento de clientes de uma empresa de manutenção veicular e venda de autopeças.

O projeto implementa CRUDs para clientes, documentos, endereços e telefones, utilizando DTOs, Services, Repositories, Controllers e regras de negócio.

---

# Objetivo das atividades

- **ATVI:** completar o CRUD básico de um sistema de gestão utilizando uma arquitetura organizada.
- **ATVII:** evoluir a API para atender os quatro níveis do Richardson Maturity Model (RMM), incluindo HATEOAS.

## Tecnologias utilizadas

| Tecnologia | Versão |
|---|---|
| Java | 17.0.20.1 |
| Spring Boot | 2.6.3 |
| Maven | 3.8.4 |
| Hibernate | 5.6.4.Final |
| Lombok | Utilizado no projeto |
| Spring Data JPA | Utilizado no projeto |
| Spring Web | Utilizado no projeto |
| Spring HATEOAS | Utilizado no projeto (ATVII) |
| H2 | Banco em memória (os dados zeram a cada reinício) |

---

## Como executar o projeto

Entre na pasta do projeto:

```
cd automanager
```

Execute a aplicação (Java 17):

```cmd
mvnw.cmd spring-boot:run
```

Quando a aplicação iniciar corretamente, será exibido:

```text
Tomcat started on port(s): 8080
```

A API estará disponível em:

```text
http://localhost:8080
```

---

# Níveis de maturidade (RMM) - ATVII

| Nível | Conceito | Como foi atendido |
|---|---|---|
| 0 | Uso do protocolo HTTP | API REST sobre HTTP, com JSON |
| 1 | URI única por recurso | `/clientes`, `/documentos`, `/enderecos`, `/telefones` (substantivos, sem verbos na URI) |
| 2 | Métodos HTTP e suas semânticas | GET consulta, POST cria, PUT atualiza, DELETE remove, cada um com o status HTTP adequado |
| 3 | HATEOAS | Respostas trazem `_links` (Spring HATEOAS, formato HAL) para navegação entre os recursos |

## Ponto de entrada

```http
GET /
```

Retorna links para `clientes`, `documentos`, `enderecos` e `telefones`. Um consumidor da API só precisa conhecer essa URL e seguir os links.

## Links retornados (`_links`)

| Resposta | Links |
|---|---|
| Item de uma listagem | `self` |
| Recurso individual | `self`, link da coleção, `atualizar`, `excluir` |
| Cliente | além dos acima, `self` de cada documento, endereço e telefone aninhados |
| Coleção | `self`, `cadastrar` |

Exemplo (`GET /clientes/1`):

```json
{
    "id": 1,
    "nome": "Laura Silva",
    "documentos": [
        {
            "id": 1,
            "tipo": "CPF",
            "numero": "12345678900",
            "_links": { "self": { "href": "http://localhost:8080/documentos/1" } }
        }
    ],
    "_links": {
        "self":      { "href": "http://localhost:8080/clientes/1" },
        "clientes":  { "href": "http://localhost:8080/clientes" },
        "atualizar": { "href": "http://localhost:8080/clientes/1" },
        "excluir":   { "href": "http://localhost:8080/clientes/1" }
    }
}
```

---

# Rotas da API

As rotas podem ser testadas com Postman, Insomnia, Thunder Client ou navegador (para GET).

| Recurso | Listar | Buscar | Cadastrar | Atualizar | Excluir |
|---|---|---|---|---|---|
| Cliente | `GET /clientes` | `GET /clientes/{id}` | `POST /clientes` | `PUT /clientes/{id}` | `DELETE /clientes/{id}` |
| Documento | `GET /documentos` | `GET /documentos/{id}` | `POST /documentos` | `PUT /documentos/{id}` | `DELETE /documentos/{id}` |
| Endereço | `GET /enderecos` | `GET /enderecos/{id}` | `POST /enderecos` | `PUT /enderecos/{id}` | `DELETE /enderecos/{id}` |
| Telefone | `GET /telefones` | `GET /telefones/{id}` | `POST /telefones` | `PUT /telefones/{id}` | `DELETE /telefones/{id}` |

## Códigos de status

| Operação | Sucesso | Falha |
|---|---|---|
| GET | 200 | 404 (não encontrado) |
| POST | 201 + header `Location` | 400 (validação), 409 (documento duplicado) |
| PUT | 200 | 400, 404, 409 |
| DELETE | 204 | 404 |

## Bodies de exemplo

### Cliente (`POST /clientes`)

```json
{
    "nome": "Laura Silva",
    "nomeSocial": "Laura",
    "dataNascimento": "2003-05-15",
    "dataCadastro": "2026-09-27",
    "documentos": [
        { "tipo": "CPF", "numero": "12345678900" }
    ],
    "endereco": {
        "estado": "SP",
        "cidade": "São José dos Campos",
        "bairro": "Centro",
        "rua": "Rua Exemplo",
        "numero": "100",
        "codigoPostal": "12200-000",
        "informacoesAdicionais": "Casa"
    },
    "telefones": [
        { "ddd": "12", "numero": "999999999" }
    ]
}
```

### Documento (`POST /documentos`)

```json
{ "tipo": "CPF", "numero": "12345678900" }
```

### Endereço (`POST /enderecos`)

```json
{
    "estado": "SP",
    "cidade": "São José dos Campos",
    "bairro": "Centro",
    "rua": "Rua Exemplo",
    "numero": "100",
    "codigoPostal": "12200-000",
    "informacoesAdicionais": "Casa"
}
```

### Telefone (`POST /telefones`)

```json
{ "ddd": "12", "numero": "999999999" }
```

---

# Regras de negócio

- Não é possível cadastrar dois documentos com o mesmo número (retorna 409).
- Ao atualizar um cliente, documentos com número já existente no próprio cliente são mantidos e apenas atualizados.
- Ao excluir um documento, endereço ou telefone que pertence a um cliente, ele é desvinculado do cliente e removido.

# Tratamento de erros

O projeto possui exceções específicas para situações como:

- Cliente não encontrado
- Documento não encontrado
- Endereço não encontrado
- Telefone não encontrado
- Documento duplicado
- Dados inválidos (validação dos campos obrigatórios)