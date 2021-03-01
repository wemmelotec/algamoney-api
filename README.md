# Algamoney-api

> Este é um projeto de estudo desenvolvido acompanhando o curso para o desenvolvimento de uma API.
> Vou utilizar as idéias das arquiteturas SOFEA e REST no desenvolvimento desta API, utilizando o JSON para integração entre o frontend e o backend.

## Tecnologias utilizadas no projeto
* Java 11
* IDE - Spring Tool Suite
* SpringBoot
* Mysql
* Flyway - utilizado para criação incremental das tabelas em meu banco.
* Git e GitHub
* Postman

## TaksLists
- [x] Criar a retrieve.
- [x] Criar o create.
- [ ] Criar o update.
- [ ] Criar o delete.
- [x] Implementei a validação de atributos desconhecidos no resquest, através da biblioteca Jackson.
- [x] Implementei o tratamento da exception handleHttpMessageNotReadable. Ocorreu quando enviei um atributo que não existe no objeto.
- [x] Implementei o BeanValidation para validar valores inválidos. Como o null no campo nome.

## Dependências do projeto
* spring-boot-starter-data-jpa
* spring-boot-starter-web
* spring-boot-devtools
* mysql-connector-java
* flyway-core
