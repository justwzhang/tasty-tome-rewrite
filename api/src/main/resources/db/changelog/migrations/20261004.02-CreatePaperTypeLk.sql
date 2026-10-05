--liquibase formatted sql

--changeset jzhang:CreatePaperTypeLk

CREATE TABLE "lk_paper_type" (
    paper_type_lk varchar(50) primary key,
    label varchar(100),
    short_label varchar(50),
    create_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by_id BIGINT REFERENCES "user" (user_id),
    updated_by_id BIGINT REFERENCES "user" (user_id)
);

insert into "lk_paper_type" (paper_type_lk, label, short_label) 
values ('graph', 'Graph Paper', 'Graph'), ('line', 'Lined Paper', 'Lined');
