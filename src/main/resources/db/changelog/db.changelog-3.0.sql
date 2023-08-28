--liquibase formatted sql

--changeset seoLeir:1
alter table users
add column image varchar(64);

--changeset seoLeir:2
alter table users_aud
    add column image varchar(64);
