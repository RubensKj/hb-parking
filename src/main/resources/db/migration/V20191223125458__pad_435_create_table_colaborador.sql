create table colaboradores(
    id_colaborador integer identity,
    primary key(id_colaborador),
    email varchar(150),
    nome varchar(35),
    data_nascimento varchar(8),
    pcd bit,
    trabalho_nortuno bit
);