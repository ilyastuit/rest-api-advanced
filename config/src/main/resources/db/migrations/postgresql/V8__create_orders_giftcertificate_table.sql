CREATE TABLE IF NOT EXISTS orders.gift_certificate
(
    id                  SERIAL PRIMARY KEY,
    price               DECIMAL(255)             NOT NULL,
    status              varchar                  NOT NULL,
    user_id             INT                      NOT NULL,
    gift_certificate_id INT                      NOT NULL,
    order_date          timestamp with time zone NOT NULL,
    last_update_date    timestamp with time zone NOT NULL
);