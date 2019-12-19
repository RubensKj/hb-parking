create table rental_term
(
    id                  BIGINT IDENTITY (1, 1) NOT NULL,
    title               VARCHAR(150)           NOT NULL,
    fileName            VARCHAR(240)           NOT NULL,
    status_rental_term  INT                    NOT NULL
);

create unique index ix_rental_term_01 on rental_term (title asc);
create unique index ix_rental_term_02 on rental_term (fileName asc);