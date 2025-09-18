# Project-Ecommerce-Monolito-
Este projeto consiste em uma solução de e-commerce desenvolvida com uma arquitetura monolítica.



### mapenado todos os endpoints(melhorar depois)
# Mapeamento de Endpoints - Ecommerce Monolito

Este documento mapeia e descreve os endpoints da API do projeto de e-commerce "Ecommerce Monolito", organizados por funcionalidade para facilitar a consulta.

---

## 1. Usuários (`localhost:8080/usuarios`)

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

---

## 2. Produtos (`localhost:8080/produtos`)

Endpoints para gerenciar os produtos disponíveis para venda.

* **`GET /produtos`**
    * **Propósito:** Listar todos os produtos.

* **`POST /produtos`**
    * **Propósito:** Criar um novo produto.

* **`POST /produtos/batch`**
    * **Propósito:** Criar vários produtos de uma só vez, usando uma lista.

* **`GET /produtos/{id}`**
    * **Propósito:** Buscar um produto pelo ID.

* **`PUT /produtos/update/{id}`**
    * **Propósito:** Atualizar um produto existente pelo ID.

* **`DELETE /produtos/delete/{id}`**
    * **Propósito:** Deletar um produto pelo ID.

---

## 3. Pedidos (`localhost:8080/pedidos`)

Endpoints para gerenciar o ciclo de vida dos pedidos.

* **`GET /pedidos`**
    * **Propósito:** Listar todos os pedidos.

* **`POST /pedidos`**
    * **Propósito:** Criar um novo pedido para um usuário com produtos específicos.

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
---

## 4. Pagamentos (`localhost:8080/pagamentos`)

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