# Conecta+ API

![CI](https://github.com/zaizeleonardo/conecta-plus-api/actions/workflows/ci.yml/badge.svg)

Backend do **Conecta+**, projeto final desenvolvido no Programa Transforma Futuros – Recode.

O sistema conecta candidatos e empresas por meio de uma API REST desenvolvida em Java e Spring Boot, utilizando MySQL para persistência dos dados.

## 🎯 Proposta

O Conecta+ foi criado para apoiar pessoas com 18 anos ou mais que buscam inserção, recolocação ou transição profissional. A plataforma organiza competências, relaciona perfis a oportunidades e apresenta um diagnóstico de compatibilidade entre candidato e vaga.

Além do percentual de compatibilidade, o sistema identifica competências que faltam e apresenta cursos relacionados para apoiar o desenvolvimento profissional.

## 🌱 ODS relacionadas

- **ODS 4 – Educação de Qualidade**
- **ODS 8 – Trabalho Decente e Crescimento Econômico**
- **ODS 10 – Redução das Desigualdades**

## 🛠️ Tecnologias

- Java
- Spring Boot
- Spring Data JPA / Hibernate
- Spring Security
- JWT
- MySQL
- Maven
- Swagger / OpenAPI (springdoc)
- JUnit 5 + Mockito
- Docker / Docker Compose
- GitHub Actions (CI)

## 🧩 Principais recursos

- Cadastro e autenticação de usuários
- Perfis de candidato, empresa e administrador
- CRUD de usuários
- CRUD de vagas
- CRUD de cursos
- Gestão de competências
- Candidaturas a vagas
- Consulta de candidatos por empresa
- Diagnóstico de compatibilidade profissional
- Recomendações de cursos com base nas competências faltantes
- Controle de acesso por perfil e JWT

## 🗄️ Banco de dados

O repositório possui a estrutura e os dados iniciais do banco em:

```text
database/
├── schema.sql
└── seed.sql
```

O `schema.sql` documenta a estrutura das tabelas e relacionamentos. O `seed.sql` contém dados de demonstração não sensíveis.

## ▶️ Executando localmente

### Opção 1 — Docker (recomendado)

Sobe o MySQL já com o `schema.sql` e o `seed.sql` carregados, e a API em seguida:

```bash
cp .env.example .env   # ajuste DB_PASSWORD e JWT_SECRET
docker compose up --build
```

- API: http://localhost:8080
- Documentação (Swagger): http://localhost:8080/swagger-ui.html

### Opção 2 — Maven

Pré-requisitos: Java 21 e um MySQL com o banco `conecta_plus` criado a partir de `database/`.

```bash
cp .env.example .env   # preencha DB_URL, DB_USERNAME, DB_PASSWORD e JWT_SECRET
./mvnw spring-boot:run          # Linux/macOS
.\mvnw spring-boot:run          # Windows PowerShell
```

As variáveis são lidas do `.env` (veja `application.properties`). **Não versione senhas, tokens, chaves ou credenciais.**

## 📖 Documentação da API

Com a aplicação rodando, acesse `/swagger-ui.html`. Para testar endpoints protegidos:

1. Faça `POST /usuarios/login` e copie o token retornado.
2. Clique em **Authorize** e cole o token.

## 🧪 Testes

```bash
./mvnw test
```

Os testes unitários de `DiagnosticoService` cobrem a regra principal do sistema (competências atendidas/faltantes, percentual de compatibilidade e recomendação de cursos) sem depender de banco de dados. Eles rodam automaticamente a cada push pelo GitHub Actions.

## 🌐 Ambiente publicado

- **API:** https://conecta-plus-api-production.up.railway.app
- **Frontend:** https://conecta-plus-front.vercel.app

A raiz da API pode retornar `403`, pois os endpoints da aplicação possuem regras de autenticação/autorização. A integração deve ser testada pelo frontend ou pelos endpoints apropriados.

## 📌 Status

Backend publicado e em funcionamento, com build e testes automatizados via GitHub Actions (Java 21).

---

Projeto desenvolvido para fins acadêmicos no Programa Transforma Futuros – Recode.
