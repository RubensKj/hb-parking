create table vehicle_models(
    id_modelo BIGINT IDENTITY(1, 1) not null,
    primary key(id_modelo),
    id_marca BIGINT not null,
    model varchar(60) not null
);