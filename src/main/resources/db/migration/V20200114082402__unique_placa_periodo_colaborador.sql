ALTER TABLE vaga_garagem DROP CONSTRAINT AK_placa_periodo;
ALTER TABLE vaga_garagem
ADD CONSTRAINT AK_placa_periodo_colaborador UNIQUE(placa, id_periodo, id_colaborador);