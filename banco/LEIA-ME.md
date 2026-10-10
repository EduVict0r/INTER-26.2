# Banco de dados — controle_estoque (MySQL 8)

| Arquivo | O que faz |
|---|---|
| `criar_banco.sql` | Cria o banco `controle_estoque` vazio. |
| `controle_estoque.sql` | Cria todas as tabelas e insere os dados de exemplo (dump de 10/10/2026). **Apaga e recria as tabelas** se elas já existirem. |

## Como importar

### Pelo MySQL Workbench
1. *File → Open SQL Script…* → `criar_banco.sql` → executar (raio ⚡).
2. Dê duplo clique no banco `controle_estoque` na lista à esquerda para selecioná-lo.
3. *File → Open SQL Script…* → `controle_estoque.sql` → executar.

### Pelo terminal (na pasta `banco`)
```
mysql -u root -p < criar_banco.sql
mysql -u root -p controle_estoque < controle_estoque.sql
```

## Conectar o sistema
Na primeira vez que o programa abre, ele pede a URL, o usuário e a senha do MySQL.
A URL padrão já aponta para este banco: `jdbc:mysql://localhost:3306/controle_estoque`.

## Tabelas
`categoria`, `cliente`, `compra`, `conta_pagar`, `forma_pagamento`, `fornecedor`,
`funcionario`, `item_compra`, `item_venda`, `movimentacao_estoque`, `produto`,
`unidade_medida`, `venda`.

As senhas dos funcionários estão salvas como hash Argon2 (não dá para ler a senha original).
Para definir uma senha nova, use `usuarios/backend/MigrarSenhas.java`.
