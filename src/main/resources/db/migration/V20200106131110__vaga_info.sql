CREATE TABLE vaga_info
(
      id BIGINT NOT NULL IDENTITY(1,1) PRIMARY KEY,
      quantidade INT NOT NULL DEFAULT 0,
      id_periodo BIGINT FOREIGN KEY REFERENCES periodo_locacao(id),
      valor FLOAT NOT NULL DEFAULT 0
  );