-- ===============================
-- ENUMS
-- ===============================
CREATE TYPE status_pedidos AS ENUM ('CRIADO', 'PROCESSANDO', 'CONCLUIDO', 'CANCELADO');
CREATE TYPE status_pagamento AS ENUM ('PENDENTE', 'APROVADO', 'RECUSADO');
CREATE TYPE tipo_forma_pagamento AS ENUM ('PIX', 'BOLETO', 'CARTAO');


-- ===============================
-- TABELA USUÁRIOS
-- ===============================
CREATE TABLE tb_usuarios (
    id BIGSERIAL PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- ===============================
-- TABELA PRODUTOS
-- ===============================
CREATE TABLE tb_produtos (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    tipo_produto VARCHAR(100) NOT NULL,
    descricao TEXT NOT NULL,
    preco NUMERIC(10,2) NOT NULL,
    item_estoque BOOLEAN NOT NULL,
    estoque INT NOT NULL
);

-- ===============================
-- TABELA PAGAMENTOS
-- ===============================
CREATE TABLE tb_pagamento (
    id BIGSERIAL PRIMARY KEY,
    valor NUMERIC(10,2) NOT NULL,
    status status_pagamento NOT NULL,
    forma_pagamento_id BIGINT UNIQUE,
    CONSTRAINT fk_pagamento_forma FOREIGN KEY (forma_pagamento_id) REFERENCES tb_forma_pagamento(id) ON DELETE CASCADE
);

-- ===============================
-- TABELA PEDIDOS
-- ===============================
CREATE TABLE tb_pedidos (
    id BIGSERIAL PRIMARY KEY,
    data_hora TIMESTAMP NOT NULL,
    status_pedidos status_pedidos NOT NULL,
    usuario_id BIGINT NOT NULL,
    pagamento_id BIGINT UNIQUE,
    CONSTRAINT fk_pedido_usuario FOREIGN KEY (usuario_id) REFERENCES tb_usuarios(id) ON DELETE CASCADE,
    CONSTRAINT fk_pedido_pagamento FOREIGN KEY (pagamento_id) REFERENCES tb_pagamento(id) ON DELETE SET NULL
);

-- ===============================
-- TABELA ITENS DO PEDIDO
-- ===============================
CREATE TABLE tb_item_pedido (
    id BIGSERIAL PRIMARY KEY,
    quantidade INT NOT NULL,
    descricao TEXT NOT NULL,
    produto_id BIGINT NOT NULL,
    pedido_id BIGINT NOT NULL,
    CONSTRAINT fk_item_produto FOREIGN KEY (produto_id) REFERENCES tb_produtos(id) ON DELETE CASCADE,
    CONSTRAINT fk_item_pedido FOREIGN KEY (pedido_id) REFERENCES tb_pedidos(id) ON DELETE CASCADE
);