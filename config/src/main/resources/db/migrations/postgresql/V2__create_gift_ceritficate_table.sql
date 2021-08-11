CREATE TABLE IF NOT EXISTS gifts.gift_certificate
(
    id               SERIAL PRIMARY KEY,
    name             varchar(100)             NOT NULL,
    description      TEXT                     NOT NULL,
    price            DECIMAL                  NOT NULL,
    duration         INTEGER                  NOT NULL,
    create_date      timestamp with time zone NOT NULL,
    last_update_date timestamp with time zone NOT NULL
);