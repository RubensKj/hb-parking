DROP INDEX vaga_garagem.idx_placa;
ALTER TABLE vaga_garagem
ADD CONSTRAINT AK_placa_periodo UNIQUE(placa, id_periodo);