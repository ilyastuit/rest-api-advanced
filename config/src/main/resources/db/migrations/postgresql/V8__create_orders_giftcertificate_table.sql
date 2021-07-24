CREATE TYPE orders.order_status AS ENUM ('paid', 'unpaid', 'canceled', 'done');

CREATE TABLE IF NOT EXISTS orders.gift_certificate
(
    id                  SERIAL PRIMARY KEY,
    price               DECIMAL                     NOT NULL,
    status              orders.order_status         NOT NULL,
    user_id             INT                         NOT NULL,
    gift_certificate_id INT                         NOT NULL,
    order_date          timestamp with time zone    NOT NULL,
    last_update_date    timestamp(3) with time zone NOT NULL
);