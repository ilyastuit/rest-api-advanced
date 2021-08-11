CREATE TABLE IF NOT EXISTS users.list
(
    id               SERIAL PRIMARY KEY,
    email            varchar(100) UNIQUE         NOT NULL,
    password         varchar(255)                NOT NULL,
    create_date      timestamp with time zone    NOT NULL,
    last_update_date timestamp with time zone NOT NULL
);