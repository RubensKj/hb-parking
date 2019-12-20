DROP INDEX ix_rental_term_02 ON rental_term;

ALTER TABLE rental_term DROP COLUMN fileName;
ALTER TABLE rental_term ADD file_name VARCHAR(240) NOT NULL;

create unique index ix_rental_term_file_name on rental_term (file_name asc);