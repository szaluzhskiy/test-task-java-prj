--liquibase formatted sql
--changeset stanislav.zaluzhsky@gmail.com:1
create table if not exists currency_rate
(
    id varchar not null
        constraint currency_rate_pkey
            primary key,
    source_currency       varchar(100) not null,
    base_currency        varchar(100) not null,
    rate         numeric not null,
    date_on date not null,
    constraint rate_per_currency_un
        unique (source_currency, base_currency, date_on)
);




