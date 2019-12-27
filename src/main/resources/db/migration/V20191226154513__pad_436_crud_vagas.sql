create table vaga_garagem
(
    id BIGINT       IDENTITY(1,1) NOT NULL  PRIMARY KEY,
    tipo_veiculo                   VARCHAR(15) NOT NULL,
    id_marca                                     BIGINT,
    id_modelo                                    BIGINT,
    cor                                     VARCHAR(15),
    placa                                    VARCHAR(7),
    id_periodo                                   BIGINT,
    id_usuario                                   BIGINT,
    status                         VARCHAR(20) NOT NULL,

    CONSTRAINT fk_marca_vaga FOREIGN KEY (id_marca)
    REFERENCES marcas (id),
    CONSTRAINT fk_modelo_vaga FOREIGN KEY (id_modelo)
    REFERENCES vehicle_models (id_modelo),
    CONSTRAINT fk_periodo_vaga FOREIGN KEY (id_periodo)
    REFERENCES periodo_locacao (id),
    CONSTRAINT fk_usuario_vaga FOREIGN KEY (id_usuario)
    REFERENCES user_auth (id)
);

 CREATE UNIQUE NONCLUSTERED INDEX idx_placa
 ON vaga_garagem(placa)
 WHERE placa IS NOT NULL;