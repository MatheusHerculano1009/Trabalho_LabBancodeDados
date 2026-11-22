-- Trabalho Laboratório de Banco de Dados (FATEC ZL)
-- Tema: Controle de Notas Fiscais 


DROP DATABASE IF EXISTS notas_db;
CREATE DATABASE notas_db;
USE notas_db;

CREATE TABLE fornecedor (
    id            INT          NOT NULL,
    cnpj          CHAR(14)     NOT NULL UNIQUE,  
    razao_social  VARCHAR(100) NOT NULL,
    telefone      VARCHAR(20),
    email         VARCHAR(100)
);

CREATE TABLE nota_fiscal (
    id               INT           NOT NULL,
    numero           INT           NOT NULL,
    id_fornecedor    INT           NOT NULL,
    data_emissao     DATE          NOT NULL,
    data_vencimento  DATE          NOT NULL,
    valor_total      DECIMAL(12,2) NOT NULL,
    status           ENUM('ABERTA', 'PAGA') NOT NULL DEFAULT 'ABERTA',
    observacao       VARCHAR(255),
);

CREATE TABLE pagamento (
    id              INT           NOT NULL,
    id_nota         INT           NOT NULL,
    data_pagamento  DATE          NOT NULL,
    valor_pago      DECIMAL(12,2) NOT NULL,
    forma           VARCHAR(20)   NOT NULL,    
);


ALTER TABLE fornecedor
    ADD CONSTRAINT pk_fornecedor PRIMARY KEY (id);
ALTER TABLE fornecedor
    MODIFY id INT NOT NULL AUTO_INCREMENT;

ALTER TABLE nota_fiscal
    ADD CONSTRAINT pk_nota_fiscal PRIMARY KEY (id);
ALTER TABLE nota_fiscal
    MODIFY id INT NOT NULL AUTO_INCREMENT;

ALTER TABLE pagamento
    ADD CONSTRAINT pk_pagamento PRIMARY KEY (id);
ALTER TABLE pagamento
    MODIFY id INT NOT NULL AUTO_INCREMENT;

ALTER TABLE nota_fiscal
    ADD CONSTRAINT fk_nota_fornecedor FOREIGN KEY (id_fornecedor)
        REFERENCES fornecedor (id);


ALTER TABLE pagamento
    ADD CONSTRAINT fk_pagamento_nota FOREIGN KEY (id_nota)
        REFERENCES nota_fiscal (id) ON DELETE CASCADE;


INSERT INTO fornecedor (cnpj, razao_social, telefone, email) VALUES
('11222333000181', 'Papelaria Central LTDA',    '(11) 3333-1000', 'vendas@papelariacentral.com.br'),
('44555666000172', 'TechParts Informática ME',  '(11) 4444-2000', 'contato@techparts.com.br'),
('77888999000163', 'Limpa Bem Distribuidora',   '(11) 5555-3000', 'financeiro@limpabem.com.br');

INSERT INTO nota_fiscal (numero, id_fornecedor, data_emissao, data_vencimento, valor_total, status, observacao) VALUES
(1001, 1, '2026-10-01', '2026-10-31',  850.00, 'PAGA',   'Material de escritório'),
(1002, 1, '2026-11-05', '2026-12-05',  420.50, 'ABERTA', NULL),
(5530, 2, '2026-09-15', '2026-10-15', 3200.00, 'ABERTA', 'Notebooks - pago em parte'),
(5531, 2, '2026-11-10', '2026-12-10', 1290.90, 'ABERTA', 'Peças de reposição'),
(  77, 3, '2026-10-20', '2026-11-19',  610.00, 'ABERTA', 'Produtos de limpeza');

INSERT INTO pagamento (id_nota, data_pagamento, valor_pago, forma) VALUES
(1, '2026-10-30',  850.00, 'BOLETO'),
(3, '2026-10-14', 2000.00, 'PIX');
