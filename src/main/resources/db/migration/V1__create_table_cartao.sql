CREATE TABLE cartao (
    numero_cartao VARCHAR(20) PRIMARY KEY,
    senha VARCHAR(255) NOT NULL,
    saldo DECIMAL(19,2) NOT NULL
); 