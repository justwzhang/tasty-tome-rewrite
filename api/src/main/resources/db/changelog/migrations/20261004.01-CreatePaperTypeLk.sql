--liquibase formatted sql

--changeset jzhang:CreatePaperTypeLk

CREATE TABLE "lk_paper_type" (
    paper_type_lk varchar(50) primary key,
    label varchar(100),
    short_label varchar(50)
);

insert into "lk_paper_type" (paper_type_lk, label, short_label) 
values ('graph', 'Graph Paper', 'Graph'), ('line', 'Lined Paper', 'Lined');
