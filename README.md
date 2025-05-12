## Pré-requisitos
### Docker 20.10+

### Docker Compose 2.5+

### Java 17 (apenas para desenvolvimento)

## Como Executar o Projeto
#### 1. Clone o repositório
   ```bash
      git clone https://github.com/seu-usuario/wishlist.git
      cd wishlist
   ````
#### 2. Construa e execute os containers
   ```bash
      cd .docker/
      docker compose up -d --build
   ```
#### 3. Acesse a API
   A aplicação estará disponível em:
   http://localhost:8080

### Endpoints Principais
#### POST /api/wishlist/{customerId}/products - Adiciona produto

#### DELETE /api/wishlist/{customerId}/products/{productId} - Remove produto

#### GET /api/wishlist/{customerId} - Lista wishlist completa

#### GET /api/wishlist/{customerId}/products/{productId} - Verifica se produto está na wishlist

## Documentação da API
#### Acesse a interface Swagger UI em:
http://localhost:8080/swagger-ui.html

## Executando os Testes
#### Testes via Docker
``` bash
    docker compose -f docker-compose.test.yml up --build --abort-on-container-exit
```
## Visualizando relatórios
#### Os relatórios dos testes serão gerados em:

target/cucumber-reports/cucumber.html

#### Configuração dos Containers
    Serviço	        Porta	Descrição
    wishlist-app	8080	Aplicação principal
    mongodb	        27017	Banco de dados MongoDB
    wishlist-test	        Container para execução de testes

## Comandos Úteis
### Parar os containers
```bash
  docker compose down
```
### Visualizar logs da aplicação
```bash
  docker compose logs -f wishlist
```
### Acessar o MongoDB
```bash
  docker exec -it mongodb mongosh -u root -p example
```
## Limpar ambiente completamente
```bash
  docker compose down -v
```

## Solução de Problemas
Se a aplicação não iniciar
Verifique os logs:

```bash
  docker compose logs wishlist
```
Confira se o MongoDB está saudável:

```bash
  docker compose ps
```
Reconstrua os containers:

```bash
  docker compose up -d --force-recreate
```
Para desenvolvimento com hot-reload
Configure sua IDE para usar o remote debug na porta 8000 quando executado via Docker.
