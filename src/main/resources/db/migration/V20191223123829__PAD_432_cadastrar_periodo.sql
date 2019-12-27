CREATE TABLE periodo_locacao
(
    id BIGINT IDENTITY(1,1) PRIMARY KEY NOT NULL,
    tipo_veiculo VARCHAR(15) NOT NULL,
    data_final DATE NOT NULL,
    data_inicial DATE NOT NULL
)