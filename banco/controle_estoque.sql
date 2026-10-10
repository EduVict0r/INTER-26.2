-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: controle_estoque
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `categoria`
--

DROP TABLE IF EXISTS `categoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categoria` (
  `id_categoria` int NOT NULL AUTO_INCREMENT,
  `nome_categoria` varchar(100) NOT NULL,
  `descricao_categoria` text,
  `status_categoria` varchar(20) DEFAULT 'ATIVO',
  PRIMARY KEY (`id_categoria`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categoria`
--

LOCK TABLES `categoria` WRITE;
/*!40000 ALTER TABLE `categoria` DISABLE KEYS */;
INSERT INTO `categoria` VALUES (1,'Copos Descartáveis','Copos plásticos e de papel de diversos tamanhos','ATIVO'),(2,'Embalagens','Marmiteiras, caixas de papelão e sacolas','ATIVO'),(3,'Higiene e Limpeza','Produtos para limpeza geral e papel higiênico/toalha','ATIVO');
/*!40000 ALTER TABLE `categoria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cliente`
--

DROP TABLE IF EXISTS `cliente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cliente` (
  `id_cliente` int NOT NULL AUTO_INCREMENT,
  `nome_cliente` varchar(150) NOT NULL,
  `cpf_cnpj_cliente` varchar(20) DEFAULT NULL,
  `telefone_cliente` varchar(20) DEFAULT NULL,
  `status_cliente` varchar(20) DEFAULT 'ATIVO',
  PRIMARY KEY (`id_cliente`),
  KEY `idx_cliente_doc` (`cpf_cnpj_cliente`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cliente`
--

LOCK TABLES `cliente` WRITE;
/*!40000 ALTER TABLE `cliente` DISABLE KEYS */;
INSERT INTO `cliente` VALUES (1,'Padaria e Confeitaria Sonho Doce','45.678.901/0001-23','(11) 98765-4321','ATIVO'),(2,'João Pedro Alves','123.456.789-00','(11) 91234-5678','ATIVO');
/*!40000 ALTER TABLE `cliente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `compra`
--

DROP TABLE IF EXISTS `compra`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `compra` (
  `id_compra` int NOT NULL AUTO_INCREMENT,
  `id_fornecedor` int NOT NULL,
  `id_funcionario` int NOT NULL,
  `data_compra` datetime DEFAULT CURRENT_TIMESTAMP,
  `valor_total_compra` decimal(10,2) DEFAULT '0.00',
  `frete` decimal(10,2) DEFAULT '0.00',
  `status_compra` varchar(20) DEFAULT 'CONCLUIDA',
  `id_funcionario_cancelou` int DEFAULT NULL,
  `data_cancelamento` datetime DEFAULT NULL,
  `motivo_cancelamento` text,
  PRIMARY KEY (`id_compra`),
  KEY `id_fornecedor` (`id_fornecedor`),
  KEY `id_funcionario` (`id_funcionario`),
  KEY `id_funcionario_cancelou` (`id_funcionario_cancelou`),
  CONSTRAINT `compra_ibfk_1` FOREIGN KEY (`id_fornecedor`) REFERENCES `fornecedor` (`id_fornecedor`),
  CONSTRAINT `compra_ibfk_2` FOREIGN KEY (`id_funcionario`) REFERENCES `funcionario` (`id_funcionario`),
  CONSTRAINT `compra_ibfk_3` FOREIGN KEY (`id_funcionario_cancelou`) REFERENCES `funcionario` (`id_funcionario`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `compra`
--

LOCK TABLES `compra` WRITE;
/*!40000 ALTER TABLE `compra` DISABLE KEYS */;
INSERT INTO `compra` VALUES (1,1,1,'2026-03-01 09:00:00',1050.00,50.00,'CONCLUIDA',NULL,NULL,NULL);
/*!40000 ALTER TABLE `compra` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `conta_pagar`
--

DROP TABLE IF EXISTS `conta_pagar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `conta_pagar` (
  `id_conta_pagar` int NOT NULL AUTO_INCREMENT,
  `id_fornecedor` int NOT NULL,
  `id_compra` int DEFAULT NULL,
  `vencimento_pagar` date NOT NULL,
  `valor_pagar` decimal(10,2) NOT NULL,
  `valor_pago_pagar` decimal(10,2) DEFAULT '0.00',
  `data_pagamento_pagar` datetime DEFAULT NULL,
  `status_pagar` varchar(20) DEFAULT 'PENDENTE',
  PRIMARY KEY (`id_conta_pagar`),
  KEY `id_fornecedor` (`id_fornecedor`),
  KEY `id_compra` (`id_compra`),
  CONSTRAINT `conta_pagar_ibfk_1` FOREIGN KEY (`id_fornecedor`) REFERENCES `fornecedor` (`id_fornecedor`),
  CONSTRAINT `conta_pagar_ibfk_2` FOREIGN KEY (`id_compra`) REFERENCES `compra` (`id_compra`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `conta_pagar`
--

LOCK TABLES `conta_pagar` WRITE;
/*!40000 ALTER TABLE `conta_pagar` DISABLE KEYS */;
INSERT INTO `conta_pagar` VALUES (1,1,1,'2026-04-01',1100.00,1100.00,'2026-03-05 10:00:00','PAGO');
/*!40000 ALTER TABLE `conta_pagar` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `forma_pagamento`
--

DROP TABLE IF EXISTS `forma_pagamento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `forma_pagamento` (
  `id_forma_pagamento` int NOT NULL AUTO_INCREMENT,
  `descricao_forma` varchar(100) NOT NULL,
  `status_forma` varchar(20) DEFAULT 'ATIVO',
  PRIMARY KEY (`id_forma_pagamento`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `forma_pagamento`
--

LOCK TABLES `forma_pagamento` WRITE;
/*!40000 ALTER TABLE `forma_pagamento` DISABLE KEYS */;
INSERT INTO `forma_pagamento` VALUES (1,'PIX','ATIVO'),(2,'ESPÉCIE','ATIVO'),(3,'DÉBITO','ATIVO'),(4,'CRÉDITO','ATIVO');
/*!40000 ALTER TABLE `forma_pagamento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fornecedor`
--

DROP TABLE IF EXISTS `fornecedor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fornecedor` (
  `id_fornecedor` int NOT NULL AUTO_INCREMENT,
  `nome_fornecedor` varchar(150) NOT NULL,
  `cpf_cnpj_fornecedor` varchar(20) DEFAULT NULL,
  `telefone_fornecedor` varchar(20) DEFAULT NULL,
  `email_fornecedor` varchar(150) DEFAULT NULL,
  `logradouro` varchar(150) DEFAULT NULL,
  `numero` varchar(20) DEFAULT NULL,
  `bairro` varchar(100) DEFAULT NULL,
  `cidade` varchar(100) DEFAULT NULL,
  `estado` varchar(2) DEFAULT NULL,
  `cep` varchar(10) DEFAULT NULL,
  `status_fornecedor` varchar(20) DEFAULT 'ATIVO',
  PRIMARY KEY (`id_fornecedor`),
  KEY `idx_fornecedor_doc` (`cpf_cnpj_fornecedor`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fornecedor`
--

LOCK TABLES `fornecedor` WRITE;
/*!40000 ALTER TABLE `fornecedor` DISABLE KEYS */;
INSERT INTO `fornecedor` VALUES (1,'Plásticos do Brasil LTDA','12.345.678/0001-90','(11) 4002-8922','contato@plasticosbrasil.com','Av. das Indústrias','1500','Distrito Industrial','São Paulo','SP','01000-000','ATIVO'),(2,'Distribuidora de Papéis Alvorada','98.765.432/0001-10','(21) 3333-4444','vendas@papeisalvorada.com','Rua das Fábricas','250','Centro','Rio de Janeiro','RJ','20000-000','ATIVO');
/*!40000 ALTER TABLE `fornecedor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `funcionario`
--

DROP TABLE IF EXISTS `funcionario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `funcionario` (
  `id_funcionario` int NOT NULL AUTO_INCREMENT,
  `nome_funcionario` varchar(150) NOT NULL,
  `senha_funcionario` varchar(255) NOT NULL,
  `email_funcionario` varchar(150) NOT NULL,
  `perfil_funcionario` varchar(50) NOT NULL,
  `status_funcionario` varchar(20) DEFAULT 'ATIVO',
  `data_cadastro_funcionario` datetime DEFAULT CURRENT_TIMESTAMP,
  `id_funcionario_cadastrou` int DEFAULT NULL,
  `data_exclusao` datetime DEFAULT NULL,
  `motivo_exclusao` text,
  PRIMARY KEY (`id_funcionario`),
  UNIQUE KEY `uq_funcionario_email` (`email_funcionario`),
  KEY `id_funcionario_cadastrou` (`id_funcionario_cadastrou`),
  CONSTRAINT `funcionario_ibfk_1` FOREIGN KEY (`id_funcionario_cadastrou`) REFERENCES `funcionario` (`id_funcionario`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `funcionario`
--

LOCK TABLES `funcionario` WRITE;
/*!40000 ALTER TABLE `funcionario` DISABLE KEYS */;
INSERT INTO `funcionario` VALUES (1,'Carlos Eduardo Silva','$argon2id$v=19$m=19456,t=2,p=1$mJgH+uqruLSRl0ZCbGEbdAZS1GV9+8hd1orW8BP4VnpL1leRNWDOjPOs+O3su6kEEDOnx2Y8gOZKf+TFiHRP+w$XljDbxta0A3bIPeSUxs+rDND8vqnM4XIvPRjqM4EBL8','carlos@loja.com','ADMINISTRADOR','ATIVO','2026-09-12 16:37:10',NULL,NULL,NULL),(2,'Ana Maria Souza','$argon2id$v=19$m=19456,t=2,p=1$G7CiJOfc8jt0tBexNXM4J0HawkQpwOfPVgx6aJMh8PilCXgKXpUI99ud1VtYxglZOPlmgOn0tVxfXUVypernjg$BncRBtXNiSX+xZeggbpKv7eG0gTwnHZ7rbIC9Ip3sjU','ana@loja.com','VENDEDOR','ATIVO','2026-09-12 16:37:10',NULL,NULL,NULL),(3,'Ewerton Cavalcanti','$argon2id$v=19$m=19456,t=2,p=1$LU5aG2zOqhvZLk1sKT9zJYaLncTjBLEJTiIioeLblz7bdGKgzOAYnw/YYZjQo4ee1Lib2u56ZBVgMTx6P/K4LA$LU9O2ow31VR5f8X00EVZ+LAkeLOb/FclAJYSIkWS5gw','EwertonS@CentralD.com','ADMINISTRADOR','ATIVO','2026-09-28 20:14:55',NULL,NULL,NULL),(4,'Maria Santos','$argon2id$v=19$m=19456,t=2,p=1$cFZDEO5bkGyKzbpweGm8cctkiH8j0rEHIOu5ITbY4b5mi8UwBTt0d4054zIPATxoZ8DQdoZUYK6WB/zMX7lZcQ$XjpBMQoYFFpJOVwCL7LcPwJiizuda5aRN0hUGT9fAmk','MariaS@CentralD.com','VENDEDOR','ATIVO','2026-09-28 21:14:04',3,NULL,NULL);
/*!40000 ALTER TABLE `funcionario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item_compra`
--

DROP TABLE IF EXISTS `item_compra`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `item_compra` (
  `id_item_compra` int NOT NULL AUTO_INCREMENT,
  `id_compra` int NOT NULL,
  `id_produto` int NOT NULL,
  `quantidade_item_compra` decimal(10,3) NOT NULL,
  `valor_unitario_item_compra` decimal(10,2) NOT NULL,
  `valor_total_item_compra` decimal(10,2) NOT NULL,
  `status_item_compra` varchar(20) DEFAULT 'ATIVO',
  `id_funcionario_excluiu` int DEFAULT NULL,
  `data_exclusao` datetime DEFAULT NULL,
  `motivo_exclusao` text,
  PRIMARY KEY (`id_item_compra`),
  KEY `id_compra` (`id_compra`),
  KEY `id_produto` (`id_produto`),
  KEY `id_funcionario_excluiu` (`id_funcionario_excluiu`),
  CONSTRAINT `item_compra_ibfk_1` FOREIGN KEY (`id_compra`) REFERENCES `compra` (`id_compra`),
  CONSTRAINT `item_compra_ibfk_2` FOREIGN KEY (`id_produto`) REFERENCES `produto` (`id_produto`),
  CONSTRAINT `item_compra_ibfk_3` FOREIGN KEY (`id_funcionario_excluiu`) REFERENCES `funcionario` (`id_funcionario`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item_compra`
--

LOCK TABLES `item_compra` WRITE;
/*!40000 ALTER TABLE `item_compra` DISABLE KEYS */;
INSERT INTO `item_compra` VALUES (1,1,1,100.000,3.50,350.00,'ATIVO',NULL,NULL,NULL),(2,1,2,28.000,25.00,700.00,'ATIVO',NULL,NULL,NULL);
/*!40000 ALTER TABLE `item_compra` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item_venda`
--

DROP TABLE IF EXISTS `item_venda`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `item_venda` (
  `id_item_venda` int NOT NULL AUTO_INCREMENT,
  `id_venda` int NOT NULL,
  `id_produto` int NOT NULL,
  `quantidade_item_venda` decimal(10,3) NOT NULL,
  `valor_unitario_item_venda` decimal(10,2) NOT NULL,
  `desconto_item_venda` decimal(10,2) DEFAULT '0.00',
  `valor_total_item_venda` decimal(10,2) NOT NULL,
  `status_item_venda` varchar(20) DEFAULT 'ATIVO',
  `id_funcionario_excluiu` int DEFAULT NULL,
  `data_exclusao` datetime DEFAULT NULL,
  `motivo_exclusao` text,
  PRIMARY KEY (`id_item_venda`),
  KEY `id_venda` (`id_venda`),
  KEY `id_produto` (`id_produto`),
  KEY `id_funcionario_excluiu` (`id_funcionario_excluiu`),
  CONSTRAINT `item_venda_ibfk_1` FOREIGN KEY (`id_venda`) REFERENCES `venda` (`id_venda`),
  CONSTRAINT `item_venda_ibfk_2` FOREIGN KEY (`id_produto`) REFERENCES `produto` (`id_produto`),
  CONSTRAINT `item_venda_ibfk_3` FOREIGN KEY (`id_funcionario_excluiu`) REFERENCES `funcionario` (`id_funcionario`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item_venda`
--

LOCK TABLES `item_venda` WRITE;
/*!40000 ALTER TABLE `item_venda` DISABLE KEYS */;
INSERT INTO `item_venda` VALUES (1,1,1,10.000,7.00,0.00,70.00,'ATIVO',NULL,NULL,NULL),(2,1,2,1.000,46.00,0.00,46.00,'ATIVO',NULL,NULL,NULL);
/*!40000 ALTER TABLE `item_venda` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `movimentacao_estoque`
--

DROP TABLE IF EXISTS `movimentacao_estoque`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `movimentacao_estoque` (
  `id_movimentacao` int NOT NULL AUTO_INCREMENT,
  `id_produto` int NOT NULL,
  `id_funcionario` int NOT NULL,
  `id_venda` int DEFAULT NULL,
  `id_compra` int DEFAULT NULL,
  `tipo_movimentacao` varchar(20) NOT NULL,
  `quantidade` decimal(10,3) NOT NULL,
  `estoque_anterior` decimal(10,3) NOT NULL,
  `estoque_posterior` decimal(10,3) NOT NULL,
  `origem` varchar(50) DEFAULT NULL,
  `data_hora_movimentacao` datetime DEFAULT CURRENT_TIMESTAMP,
  `observacao` text,
  PRIMARY KEY (`id_movimentacao`),
  KEY `id_produto` (`id_produto`),
  KEY `id_funcionario` (`id_funcionario`),
  KEY `id_venda` (`id_venda`),
  KEY `id_compra` (`id_compra`),
  KEY `idx_movimentacao_data` (`data_hora_movimentacao`),
  CONSTRAINT `movimentacao_estoque_ibfk_1` FOREIGN KEY (`id_produto`) REFERENCES `produto` (`id_produto`),
  CONSTRAINT `movimentacao_estoque_ibfk_2` FOREIGN KEY (`id_funcionario`) REFERENCES `funcionario` (`id_funcionario`),
  CONSTRAINT `movimentacao_estoque_ibfk_3` FOREIGN KEY (`id_venda`) REFERENCES `venda` (`id_venda`),
  CONSTRAINT `movimentacao_estoque_ibfk_4` FOREIGN KEY (`id_compra`) REFERENCES `compra` (`id_compra`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movimentacao_estoque`
--

LOCK TABLES `movimentacao_estoque` WRITE;
/*!40000 ALTER TABLE `movimentacao_estoque` DISABLE KEYS */;
INSERT INTO `movimentacao_estoque` VALUES (1,1,1,NULL,1,'ENTRADA',100.000,60.000,160.000,'COMPRA','2026-03-01 09:30:00','Entrada referente a compra #1'),(2,1,2,1,NULL,'SAIDA',10.000,160.000,150.000,'VENDA','2026-03-10 14:30:00','Saída referente a venda #1');
/*!40000 ALTER TABLE `movimentacao_estoque` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `produto`
--

DROP TABLE IF EXISTS `produto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `produto` (
  `id_produto` int NOT NULL AUTO_INCREMENT,
  `nome_produto` varchar(50) DEFAULT NULL,
  `codigo_produto` varchar(50) DEFAULT NULL,
  `codigo_barras` varchar(50) DEFAULT NULL,
  `ncm_produto` varchar(20) DEFAULT NULL,
  `descricao_produto` varchar(255) DEFAULT NULL,
  `custo_produto` decimal(10,2) DEFAULT '0.00',
  `preco_venda` decimal(10,2) DEFAULT '0.00',
  `estoque_atual` decimal(10,3) DEFAULT '0.000',
  `estoque_minimo` decimal(10,3) DEFAULT '0.000',
  `estoque_maximo` decimal(10,3) DEFAULT '0.000',
  `status_produto` varchar(20) DEFAULT 'ATIVO',
  `id_unidade` int NOT NULL,
  `id_categoria` int NOT NULL,
  `id_fornecedor` int DEFAULT NULL,
  `id_funcionario_excluiu` int DEFAULT NULL,
  `data_exclusao` datetime DEFAULT NULL,
  `motivo_exclusao` text,
  PRIMARY KEY (`id_produto`),
  KEY `id_unidade` (`id_unidade`),
  KEY `id_categoria` (`id_categoria`),
  KEY `id_fornecedor` (`id_fornecedor`),
  KEY `id_funcionario_excluiu` (`id_funcionario_excluiu`),
  KEY `idx_produto_cod` (`codigo_produto`),
  KEY `idx_produto_barras` (`codigo_barras`),
  CONSTRAINT `produto_ibfk_1` FOREIGN KEY (`id_unidade`) REFERENCES `unidade_medida` (`id_unidade`),
  CONSTRAINT `produto_ibfk_2` FOREIGN KEY (`id_categoria`) REFERENCES `categoria` (`id_categoria`),
  CONSTRAINT `produto_ibfk_3` FOREIGN KEY (`id_fornecedor`) REFERENCES `fornecedor` (`id_fornecedor`),
  CONSTRAINT `produto_ibfk_4` FOREIGN KEY (`id_funcionario_excluiu`) REFERENCES `funcionario` (`id_funcionario`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `produto`
--

LOCK TABLES `produto` WRITE;
/*!40000 ALTER TABLE `produto` DISABLE KEYS */;
INSERT INTO `produto` VALUES (1,'Copo Plástico 200ml','COP200','7891234567890','39241000','Copo Plástico 200ml Tırman (Pacote com 100 un)',3.50,7.00,150.000,20.000,500.000,'ATIVO',3,1,1,NULL,NULL,NULL),(2,'Marmitex Isopor 500ml','MARM500','7891234567891','39231090','Marmitex Isopor 500ml (Caixa com 100 un)',25.00,48.00,40.000,10.000,100.000,'ATIVO',2,2,1,NULL,NULL,NULL),(3,'Detergente Neutro 5L','DETERG5L','7891234567892','34022000','Detergente Neutro Galão 5 Litros',12.00,22.50,25.000,5.000,50.000,'ATIVO',5,3,2,NULL,NULL,NULL);
/*!40000 ALTER TABLE `produto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `unidade_medida`
--

DROP TABLE IF EXISTS `unidade_medida`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `unidade_medida` (
  `id_unidade` int NOT NULL AUTO_INCREMENT,
  `sigla_unidade` varchar(10) NOT NULL,
  `descricao_unidade` varchar(100) NOT NULL,
  PRIMARY KEY (`id_unidade`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `unidade_medida`
--

LOCK TABLES `unidade_medida` WRITE;
/*!40000 ALTER TABLE `unidade_medida` DISABLE KEYS */;
INSERT INTO `unidade_medida` VALUES (1,'UN','UNIDADE'),(2,'CX','CAIXA'),(3,'PCT','PACOTE'),(4,'KG','KILOGRAMA'),(5,'L','LITRO');
/*!40000 ALTER TABLE `unidade_medida` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `venda`
--

DROP TABLE IF EXISTS `venda`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `venda` (
  `id_venda` int NOT NULL AUTO_INCREMENT,
  `id_cliente` int DEFAULT NULL,
  `id_funcionario` int NOT NULL,
  `id_forma_pagamento` int NOT NULL,
  `data_venda` datetime DEFAULT CURRENT_TIMESTAMP,
  `valor_total_venda` decimal(10,2) DEFAULT '0.00',
  `desconto_venda` decimal(10,2) DEFAULT '0.00',
  `status_venda` varchar(20) DEFAULT 'CONCLUIDA',
  `id_funcionario_cancelou` int DEFAULT NULL,
  `data_cancelamento` datetime DEFAULT NULL,
  `motivo_cancelamento` text,
  PRIMARY KEY (`id_venda`),
  KEY `id_cliente` (`id_cliente`),
  KEY `id_funcionario` (`id_funcionario`),
  KEY `id_forma_pagamento` (`id_forma_pagamento`),
  KEY `id_funcionario_cancelou` (`id_funcionario_cancelou`),
  CONSTRAINT `venda_ibfk_1` FOREIGN KEY (`id_cliente`) REFERENCES `cliente` (`id_cliente`),
  CONSTRAINT `venda_ibfk_2` FOREIGN KEY (`id_funcionario`) REFERENCES `funcionario` (`id_funcionario`),
  CONSTRAINT `venda_ibfk_3` FOREIGN KEY (`id_forma_pagamento`) REFERENCES `forma_pagamento` (`id_forma_pagamento`),
  CONSTRAINT `venda_ibfk_4` FOREIGN KEY (`id_funcionario_cancelou`) REFERENCES `funcionario` (`id_funcionario`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `venda`
--

LOCK TABLES `venda` WRITE;
/*!40000 ALTER TABLE `venda` DISABLE KEYS */;
INSERT INTO `venda` VALUES (1,1,2,1,'2026-03-10 14:30:00',116.00,0.00,'CONCLUIDA',NULL,NULL,NULL);
/*!40000 ALTER TABLE `venda` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-10 17:02:40
