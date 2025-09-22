# Project-Ecommerce-Monolítico
Este projeto é uma aplicação de e-commerce construída com a arquitetura monolítica, utilizando o framework Spring Boot. O objetivo é demonstrar a construção de uma API REST completa para gerenciar pedidos, produtos, usuários e pagamentos, com foco em boas práticas de design de software, testes unitários, e padrões de persistência com JPA. A aplicação foi desenvolvida com uma abordagem de "código limpo", garantindo a separação de responsabilidades entre as camadas de Controller, Service e Repository.

## Diagrama de Arquitetura
A arquitetura do projeto é monolítica e segue o padrão de camadas, onde cada componente tem uma responsabilidade específica.

- Arquitetura e Estrutura da aplicação:
![App Screenshot](https://github.com/ThallysCezar/Project-Ecommerce-Monolito/blob/main/src/main/resources/imagesToReadMe/Arquitetura.jpeg)

- Fluxograma da aplicação:
![App Screenshot](https://github.com/ThallysCezar/Project-Ecommerce-Monolito/blob/main/src/main/resources/imagesToReadMe/Fluxograma.jpg)


## Tecnologias Utilizadas
A aplicação é construída com as seguintes tecnologias:

**Back-end:** Java, SpringBoot, PostgreSQL + DBeaver, RESTEasy para APIs REST, Flyway para migração de banco de dados, Docker para containerização, Swagger OpenAPI para documentação, JUnit 5 e Mockito para testes unitários.

## Guia de Instalação e Execução
Para executar a aplicação, a forma mais fácil e recomendada é através do Docker Compose, que gerencia a inicialização do banco de dados (PostgreSQL) e da aplicação em um único comando.

**Pré-requisitos:**
* Git
* Docker e Docker Compose

**Passos:**

1.  **Clone o repositório:**
    ```sh
    git clone https://github.com/ThallysCezar/Project-Ecommerce-Monolito
    cd [pasta do projeto]
    ```

2.  **Inicie a aplicação com Docker Compose:**
    ```sh
    docker-compose up --build
    ```
    Isso irá construir a imagem da sua aplicação, criar um contêiner para o PostgreSQL e iniciar o serviço.

3.  **Acesse a API:**
    Após a inicialização, a documentação da API estará disponível em:
    `http://localhost:8080/swagger-ui.html`


## Mapeamento de Endpoints - Ecommerce Monolítico
Este documento mapeia e descreve os endpoints da API do projeto de e-commerce "Ecommerce Monolito", organizados por funcionalidade para facilitar a consulta.

### 1. Usuários (`localhost:8080/usuarios`)
Endpoints dedicados ao gerenciamento de usuários no sistema.

* **`GET /usuarios`**
    * **Propósito:** Listar todos os usuários cadastrados.

* **`POST /usuarios`**
    * **Propósito:** Criar um novo usuário.
    * **Exemplo de Corpo da Requisição:**
        ```json
        {
          "userName": "joao_silva",
          "email": "joao.silva@example.com",
          "password": "senha_segura_123"
        }
        ```

* **`GET /usuarios/{id}`**
    * **Propósito:** Buscar um usuário específico pelo ID.

* **`GET /usuarios/email/{email}`**
    * **Propósito:** Buscar um usuário pelo seu endereço de e-mail.

* **`PUT /usuarios/update/{id}`**
    * **Propósito:** Atualizar as informações de um usuário pelo ID.

* **`DELETE /usuarios/delete/{id}`**
    * **Propósito:** Deletar um usuário pelo ID.


### 2. Produtos (`localhost:8080/produtos`)
Endpoints para gerenciar os produtos disponíveis para venda.

* **`GET /produtos`**
    * **Propósito:** Listar todos os produtos.

* **`POST /produtos`**
    * **Propósito:** Criar um novo produto.
    * **Exemplo de Corpo da Requisição:**
        ```json
        {
          "titulo": "Monitor Gamer 27 Polegadas",
	      "tipoProduto": "Eletrônicos",
	      "descricao": "Monitor com tela curva, 144Hz e tempo de resposta de 1ms, ideal para jogos competitivos.",
	      "preco": 1800.00,
	      "itemEstoque": true,
	      "estoque": 25
        }
        ```

* **`POST /produtos/batch`**
    * **Propósito:** Criar vários produtos de uma só vez, usando uma lista.
    * **Exemplo de Corpo da Requisição:**
        ```json
        [
            {
                "titulo": "Monitor Gamer 27 Polegadas",
                "tipoProduto": "Eletrônicos",
                "descricao": "Monitor com tela curva, 144Hz e tempo de resposta de 1ms, ideal para jogos competitivos.",
                "preco": 1800.00,
                "itemEstoque": true,
                "estoque": 25
            },
            {
                "titulo": "Teclado Mecânico RGB",
                "tipoProduto": "Acessórios",
                "descricao": "Teclado com switches mecânicos e iluminação RGB customizável.",
                "preco": 450.00,
                "itemEstoque": true,
                "estoque": 150
            },
            {
                "titulo": "Cadeira Gamer Ergonômica",
                "tipoProduto": "Móveis",
                "descricao": "Cadeira com design ergonômico, suporte lombar ajustável e apoio de braço 4D.",
                "preco": 850.00,
                "itemEstoque": true,
                "estoque": 40
            }
        ]
        ```

* **`GET /produtos/{id}`**
    * **Propósito:** Buscar um produto pelo ID.

* **`PUT /produtos/update/{id}`**
    * **Propósito:** Atualizar um produto existente pelo ID.

* **`DELETE /produtos/delete/{id}`**
    * **Propósito:** Deletar um produto pelo ID.


### 3. Pedidos (`localhost:8080/pedidos`)
Endpoints para gerenciar o ciclo de vida dos pedidos.

* **`GET /pedidos`**
    * **Propósito:** Listar todos os pedidos.

* **`POST /pedidos`**
    * **Propósito:** Criar um novo pedido para um usuário com produtos específicos.
    * **Exemplo de Corpo da Requisição:**
        ```json
        {
            "usuario": {
                "id": 1
            },
            "itens": [
                {
                    "quantidade": 3,
                    "descricao": "Cadeira Gamer Ergonômica",
                    "produto": {
                        "id": 3
                    }
                }
            ]
        }
        ```

* **`GET /pedidos/{id}`**
    * **Propósito:** Buscar um pedido pelo ID.

* **`GET /pedidos/user/{id}`**
    * **Propósito:** Buscar todos os pedidos associados a um usuário.

* **`PUT /pedidos/update/{id}`**
    * **Propósito:** Atualizar um pedido existente.

* **`PATCH /pedidos/{id}/confirmarPedido`**
    * **Propósito:** Alterar o status de um pedido para "confirmado".

* **`PATCH /pedidos/cancelarPedido/{id}`**
    * **Propósito:** Alterar o status de um pedido para "cancelado".

* **`DELETE /pedidos/delete/{id}`**
    * **Propósito:** Deletar um pedido pelo ID.

* **`POST /pedidos/{id}/pagamento`**
  * **Propósito:** Criar um novo pagamento para o pedido com base no ID, aceitando diferentes métodos (PIX, Boleto, Cartão).
    * **Exemplo de Corpo da Requisição para Boleto:**
        ```json
        {
        "valor": 250.99,
        "tipoPagamento": "BOLETO"
        }
        ```

    * **Exemplo de Corpo da Requisição para Cartão de Crédito:**
        ```json
        {
        "valor": 7500.00,
        "tipoPagamento": "CARTAO_CREDITO",
        "nomeTitularCartao": "JOAO DA SILVA",
        "numeroCartao": "1234567890123456",
        "expiracaoCartao": "12/26",
        "codigoCartao": "123"
        }
        ```

    * **Exemplo de Corpo da Requisição para Pix:**
        ```json
        {
        "valor": 3200.50,
        "tipoPagamento": "PIX",
        "chavePix": "joao.silva@email.com"
        }
        ```


### 4. Pagamentos (`localhost:8080/pagamentos`)
Endpoints para gerenciar e processar pagamentos.

* **`GET /pagamentos`**
    * **Propósito:** Listar todos os pagamentos.

* **`GET /pagamentos/{id}`**
    * **Propósito:** Buscar um pagamento pelo ID.

* **`GET /pagamentos/pedido/{id}`**
    * **Propósito:** Buscar o pagamento associado a um pedido específico.

* **`PUT /pagamentos/update/{id}`**
    * **Propósito:** Atualizar os dados de um pagamento.

* **`PATCH /pagamentos/{id}/confirmar`**
    * **Propósito:** Confirmar um pagamento, alterando seu status.

* **`DELETE /pagamentos/delete/{id}`**
    * **Propósito:** Deletar um pagamento. 


### Resultados dos Testes

O projeto conta com uma suíte de testes unitários robusta, utilizando **JUnit 5** e **Mockito**. Para rodar os testes e verificar a cobertura do código, execute o seguinte comando:

```sh
mvn test
```