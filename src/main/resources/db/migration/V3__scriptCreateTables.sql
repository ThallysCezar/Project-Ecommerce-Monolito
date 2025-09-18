-- ===============================
-- TABELA USUÁRIOS
-- ===============================
CREATE TABLE IF NOT EXISTS public.tb_usuarios (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    user_name VARCHAR(255) NOT NULL
);


-- ===============================
-- TABELA PRODUTOS
-- ===============================
CREATE TABLE IF NOT EXISTS public.tb_produtos (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    tipo_produto VARCHAR(255) NOT NULL,
    descricao VARCHAR(255) NULL,
    preco FLOAT8 NOT NULL,
    item_estoque BOOLEAN NOT NULL DEFAULT TRUE,
    estoque INT NOT NULL
);


-- ===============================
-- TABELA PAGAMENTOS
-- ===============================
CREATE TABLE IF NOT EXISTS public.tb_pagamentos (
    id BIGSERIAL PRIMARY KEY,
    valor FLOAT8 NOT NULL,
    status_pagamento VARCHAR(255) NOT NULL,
    tipo_pagamento VARCHAR(255) NOT NULL,

    -- Campos para CARTAO_CREDITO
    nome_titular_cartao VARCHAR(255) NULL,
    numero_cartao VARCHAR(255) NULL,
    expiracao_cartao VARCHAR(255) NULL,
    codigo_cartao VARCHAR(255) NULL,

    -- Campo para PIX
    chave_pix VARCHAR(255) NULL,

    -- Campo para BOLETO
    codigo_de_barras_boleto VARCHAR(255) NULL
);


-- ===============================
-- TABELA PEDIDOS
-- ===============================
CREATE TABLE IF NOT EXISTS public.tb_pedidos (
    id BIGSERIAL PRIMARY KEY,
    data_hora TIMESTAMP(6) NOT NULL,
    status_pedidos VARCHAR(255) NOT NULL,
    usuario_id BIGINT NULL, -- Este campo agora é opcional
    pagamento_id BIGINT UNIQUE NULL, -- Referencia para o pagamento (opcional no inicio)

    CONSTRAINT fk_pedido_usuario FOREIGN KEY (usuario_id) REFERENCES public.tb_usuarios(id),
    CONSTRAINT fk_pedido_pagamento FOREIGN KEY (pagamento_id) REFERENCES public.tb_pagamentos(id),
    CONSTRAINT chk_status_pedidos CHECK (status_pedidos IN ('CRIADO', 'AGUARDANDO_PAGAMENTO', 'CONFIRMADO', 'CANCELADO', 'PAGO'))
);


-- ===============================
-- TABELA ITENS DO PEDIDO
-- ===============================
CREATE TABLE IF NOT EXISTS public.tb_item_pedido (
    id BIGSERIAL PRIMARY KEY,
    quantidade INT NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    pedido_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,

    CONSTRAINT fk_item_pedido FOREIGN KEY (pedido_id) REFERENCES public.tb_pedidos(id),
    CONSTRAINT fk_item_produto FOREIGN KEY (produto_id) REFERENCES public.tb_produtos(id)
);