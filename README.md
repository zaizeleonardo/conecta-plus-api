# Conecta+ API

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

### Pré-requisitos

- Java instalado
- Git
- Banco MySQL disponível

### Build

O projeto utiliza Maven Wrapper. No Windows PowerShell:

```powershell
.\mvnw clean package
```

Para execução pelo ambiente de desenvolvimento, importe o projeto Maven no Spring Tools for Eclipse (STS) ou utilize o mecanismo de execução da IDE.

### Configuração

Configure as variáveis/propriedades necessárias para conexão com o MySQL e para o JWT no ambiente local. **Não versione senhas, tokens, chaves ou credenciais.**

## 🌐 Ambiente publicado

- **API:** https://conecta-plus-api-production.up.railway.app
- **Frontend:** https://conecta-plus-front.vercel.app

A raiz da API pode retornar `403`, pois os endpoints da aplicação possuem regras de autenticação/autorização. A integração deve ser testada pelo frontend ou pelos endpoints apropriados.

## 📌 Status

Backend validado com `BUILD SUCCESS` usando Maven Wrapper e Java 25.

---

Projeto desenvolvido para fins acadêmicos no Programa Transforma Futuros – Recode.
