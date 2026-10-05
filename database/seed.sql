-- Conecta+ - Seed inicial
-- Dados de demonstração não sensíveis.
-- Não inclui usuários, senhas, tokens ou candidaturas do ambiente de testes.

USE conecta_plus;

SET NAMES utf8mb4;

-- =========================
-- Competências
-- =========================
INSERT INTO competencias (id, nome, categoria, descricao) VALUES
(1, 'Java', 'Programação', 'Linguagem de programação utilizada no desenvolvimento de sistemas.'),
(2, 'SQL', 'Banco de Dados', 'Linguagem utilizada para consultar e manipular bancos de dados.'),
(3, 'Git', 'Versionamento', 'Sistema de controle de versão utilizado para gerenciar código-fonte.'),
(4, 'Spring Boot', 'Backend', 'Framework Java utilizado para desenvolvimento de aplicações e APIs.'),
(5, 'Docker', 'DevOps', 'Plataforma utilizada para criar e executar aplicações em containers.'),
(11, 'Python', 'Programação', 'Linguagem utilizada em desenvolvimento, automação e análise de dados.');

-- =========================
-- Cursos
-- =========================
INSERT INTO cursos (id, nome, plataforma, descricao, url) VALUES
(1, 'Spring Boot para Iniciantes', 'Alura',
 'Curso introdutório de desenvolvimento de APIs REST com Spring Boot.',
 'https://www.alura.com.br/'),
(2, 'Docker para Desenvolvedores', 'DIO',
 'Curso sobre containers Docker e utilização no desenvolvimento de aplicações.',
 'https://www.dio.me/'),
(3, 'Python para Análise de Dados', 'DIO',
 'Curso voltado ao aprendizado de Python aplicado à análise e manipulação de dados.',
 'https://www.dio.me/');

-- =========================
-- Relação curso x competência
-- =========================
INSERT INTO curso_competencia (curso_id, competencia_id) VALUES
(1, 4),
(2, 5),
(3, 11);

-- =========================
-- Vagas de demonstração
-- empresa_id permanece NULL porque os usuários/empresas
-- do ambiente de testes não fazem parte deste seed.
-- =========================
INSERT INTO vagas
(id, titulo, empresa, descricao, cidade, modalidade, salario, empresa_id)
VALUES
(1, 'Desenvolvedor Backend Java', 'Empresa Conecta+',
 'Desenvolvimento e manutenção de aplicações backend utilizando Java e Spring Boot.',
 'São Paulo', 'Remoto', 4500.00, NULL),
(2, 'Analista de Dados Júnior', 'Empresa Conecta+',
 'Atuação com análise de dados, consultas SQL e desenvolvimento de soluções utilizando Python.',
 'São Paulo', 'Híbrido', 3500.00, NULL);

-- =========================
-- Relação vaga x competência
-- =========================
INSERT INTO vaga_competencia (vaga_id, competencia_id) VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(1, 5),
(2, 2),
(2, 3),
(2, 11);
