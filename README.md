# Mini Autorizador

## Requisitos

- Java 21 ou superior
- Maven 3.6+
- Docker e Docker Compose (para banco de dados MySQL)
- (Opcional) Plugin Lombok instalado na IDE

## Configuração do Banco de Dados

O projeto utiliza um banco MySQL via Docker Compose. Para subir o banco:
```sh
cd docker
docker-compose up -d
```

## Como rodar a aplicação

1. Suba o banco de dados conforme instrução acima.
2. Volte para a raiz do projeto e execute:

```sh
mvn spring-boot:run
```

A aplicação estará disponível em [Swagger](http://localhost:8080/swagger-ui/index.html).

## Rodando os testes

Para executar todos os testes automatizados:

```sh
mvn clean test
```

## Gerando relatório de cobertura de testes

Para gerar o relatório de cobertura com o JaCoCo:

```sh
mvn clean test jacoco:report
```

O relatório HTML estará disponível em:

```
target/site/jacoco/index.html
```
Abra esse arquivo no navegador para visualizar a cobertura de código.

## Observações

- Certifique-se de que o plugin Lombok está instalado na sua IDE para evitar erros de reconhecimento de código gerado.
- As configurações do banco estão em `src/main/resources/application.yml`.
- Para customizar portas ou credenciais do banco, edite o arquivo `docker/docker-compose.yml` e o `application.yml`. 
