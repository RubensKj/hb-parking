ALTER TABLE marcas
ADD CONSTRAINT UK_tipo_nome UNIQUE (tipo_veiculo, nome);