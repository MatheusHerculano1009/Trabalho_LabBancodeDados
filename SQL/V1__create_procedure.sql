USE assistencia_tecnica;

DELIMITER $$
CREATE PROCEDURE sp_inserir_nota_fiscal(
    IN p_numero INT,
    IN p_dataEmissao DATE,
    IN p_fornecedor VARCHAR(100),
    IN p_valorTotal DECIMAL(12,2),
    IN p_dataVencimento DATE,
    IN p_observacao VARCHAR(255)
)
BEGIN
    IF p_valorTotal <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Valor total deve ser maior que zero';
    END IF;

    IF p_dataVencimento < p_dataEmissao THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Vencimento não pode ser anterior à emissão';
    END IF;

    IF EXISTS (SELECT 1 FROM notas_fiscais
               WHERE numero = p_numero AND fornecedor = p_fornecedor) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Nota já cadastrada para este fornecedor';
    END IF;

    INSERT INTO notas_fiscais (numero, dataEmissao, fornecedor, valorTotal, dataVencimento, observacao)
    VALUES (p_numero, p_dataEmissao, p_fornecedor, p_valorTotal, p_dataVencimento, p_observacao);
END$$
DELIMITER ;