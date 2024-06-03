# Desafio Backend

Neste desafio, você será responsável por implementar uma API REST para um sistema simples de contas a pagar. O sistema permitirá realizar operações CRUD em contas a pagar, além de alterar a situação delas quando o pagamento for efetuado, obter informações sobre as contas cadastradas no banco de dados e importar um lote de contas a pagar de um arquivo CSV.

## Requisitos Gerais

1. Utilize a linguagem de programação Java, versão 17 ou superior.
2. Utilize Spring Boot.
3. Utilize o banco de dados PostgreSQL.
4. A aplicação deve ser executada em um container Docker.
5. Todos os serviços necessários para executar a aplicação, incluindo aplicação, banco de dados, etc., devem ser orquestrados utilizando Docker Compose.
6. O código do projeto deve ser hospedado no GitHub ou GitLab.
7. Utilize um mecanismo de autenticação.
8. Organize o projeto com Domain Driven Design.
9. Utilize o Flyway para criar a estrutura de banco de dados.
10. Utilize JPA.
11. Todas as APIs de consulta devem ser paginadas.

## Requisitos Específicos

1. Crie uma tabela no banco de dados para armazenar as contas a pagar. Deve incluir, no mínimo, os seguintes campos: (Faça a tipagem conforme achar adequado)
    - id
    - data_vencimento
    - data_pagamento
    - valor
    - descricao
    - situacao
2. Implemente a entidade "Conta" na aplicação, de acordo com a tabela criada anteriormente.
3. Implemente as seguintes APIs:
    - Cadastrar conta;
    - Atualizar conta;
    - Alterar a situação da conta;
    - Obter a lista de contas a pagar, com filtro de data de vencimento e descrição;
    - Obter conta filtrando o id;
    - Obter valor total pago por período.
4. Implemente um mecanismo para importar contas a pagar via arquivo CSV.
    - O arquivo será consumido via API.
5. Implemente testes unitários.

---

![image](https://github.com/brunocrodriguessouza/payflow/assets/1760665/aa953f27-2604-43d7-8472-ea48653863cf)


