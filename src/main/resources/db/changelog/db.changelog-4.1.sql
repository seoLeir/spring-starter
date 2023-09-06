--liquibase formatted sql

--changeset seoLeir:1
alter table users_aud
alter column password type varchar(128)