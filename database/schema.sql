-- CONECTA+
-- Schema do banco de dados
-- Estrutura baseada no DDL real obtido no banco conecta_plus.
-- Nenhum DROP TABLE é utilizado neste arquivo.

USE `conecta_plus`;

CREATE TABLE IF NOT EXISTS `usuarios` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `email` varchar(150) NOT NULL,
  `senha` varchar(255) NOT NULL,
  `telefone` varchar(20) DEFAULT NULL,
  `cidade` varchar(100) DEFAULT NULL,
  `objetivo_profissional` varchar(150) DEFAULT NULL,
  `data_cadastro` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `perfil` varchar(20) NOT NULL DEFAULT 'USUARIO',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
);

CREATE TABLE IF NOT EXISTS `competencias` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `categoria` varchar(100) DEFAULT NULL,
  `descricao` text,
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `cursos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nome` varchar(150) NOT NULL,
  `plataforma` varchar(100) DEFAULT NULL,
  `descricao` text,
  `url` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `vagas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `titulo` varchar(150) NOT NULL,
  `empresa` varchar(150) DEFAULT NULL,
  `descricao` text,
  `cidade` varchar(100) DEFAULT NULL,
  `modalidade` varchar(50) DEFAULT NULL,
  `salario` decimal(10,2) DEFAULT NULL,
  `empresa_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_vagas_empresa` (`empresa_id`),
  CONSTRAINT `fk_vagas_empresa`
    FOREIGN KEY (`empresa_id`) REFERENCES `usuarios` (`id`)
);

CREATE TABLE IF NOT EXISTS `candidaturas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `usuario_id` bigint NOT NULL,
  `vaga_id` bigint NOT NULL,
  `data_candidatura` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` varchar(30) NOT NULL DEFAULT 'ENVIADA',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_usuario_vaga` (`usuario_id`,`vaga_id`),
  KEY `fk_candidatura_vaga` (`vaga_id`),
  CONSTRAINT `fk_candidatura_usuario`
    FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `fk_candidatura_vaga`
    FOREIGN KEY (`vaga_id`) REFERENCES `vagas` (`id`)
);

CREATE TABLE IF NOT EXISTS `usuario_competencia` (
  `usuario_id` bigint NOT NULL,
  `competencia_id` bigint NOT NULL,
  PRIMARY KEY (`usuario_id`,`competencia_id`),
  KEY `competencia_id` (`competencia_id`),
  CONSTRAINT `usuario_competencia_ibfk_1`
    FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `usuario_competencia_ibfk_2`
    FOREIGN KEY (`competencia_id`) REFERENCES `competencias` (`id`)
);

CREATE TABLE IF NOT EXISTS `vaga_competencia` (
  `vaga_id` bigint NOT NULL,
  `competencia_id` bigint NOT NULL,
  PRIMARY KEY (`vaga_id`,`competencia_id`),
  KEY `competencia_id` (`competencia_id`),
  CONSTRAINT `vaga_competencia_ibfk_1`
    FOREIGN KEY (`vaga_id`) REFERENCES `vagas` (`id`),
  CONSTRAINT `vaga_competencia_ibfk_2`
    FOREIGN KEY (`competencia_id`) REFERENCES `competencias` (`id`)
);

CREATE TABLE IF NOT EXISTS `curso_competencia` (
  `curso_id` bigint NOT NULL,
  `competencia_id` bigint NOT NULL,
  PRIMARY KEY (`curso_id`,`competencia_id`),
  KEY `competencia_id` (`competencia_id`),
  CONSTRAINT `curso_competencia_ibfk_1`
    FOREIGN KEY (`curso_id`) REFERENCES `cursos` (`id`),
  CONSTRAINT `curso_competencia_ibfk_2`
    FOREIGN KEY (`competencia_id`) REFERENCES `competencias` (`id`)
);
