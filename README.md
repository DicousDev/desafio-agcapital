# Desafio técnico AG Capital

## Requisitos

- Java 17
- Git
- Maven 3.6.3
- Docker 20.10+
- Docker-compose

## Arquitetura

A arquitetura do projeto foi decidida conforme as necessidades e a complexidade do mesmo, seguindo principalmente príncipio KISS, Domain driven design (DDD) e boas práticas.


## Executando localmente

1. Certifique-se que Docker esteja executando para conseguir subir os containers.
2. Clone o repositório

```shell 
git clone git@github.com:DicousDev/desafio-agcapital.git
```

3. Executar seguinte comando para subir os containers.
   Vale lembrar que pode demorar para as aplicações inicializarem.

```shell 
docker-compose up -d
```
Irá subir dois container. PostgreSQL e API.

API estará disponível em http://localhost:8080

Documentação da API em http://localhost:8080/swagger-ui/index.html#/