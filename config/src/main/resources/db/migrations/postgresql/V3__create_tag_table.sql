CREATE TABLE IF NOT EXISTS gifts.tag
(
    id   SERIAL PRIMARY KEY,
    name varchar(100) UNIQUE NOT NULL
);