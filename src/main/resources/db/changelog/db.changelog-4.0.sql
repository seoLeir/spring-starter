--liquibase formatted sql

--changeset seoLeir:1
alter table users
add column password varchar(128) default '{noop}123';

--changeset seoLeir:2
alter table users_aud
    add column password varchar(64);
