CREATE TABLE IF NOT EXISTS gifts.gift_certificate_tag
(
    id                  SERIAL PRIMARY KEY,
    gift_certificate_id INT NOT NULL,
    tag_id              INT NOT NULL,

    CONSTRAINT fk_gift_certificate
        FOREIGN KEY (gift_certificate_id)
            REFERENCES gifts.gift_certificate (id)
            ON DELETE CASCADE
            ON UPDATE NO ACTION,

    CONSTRAINT fk_tag
        FOREIGN KEY (tag_id)
            REFERENCES gifts.tag (id)
            ON DELETE CASCADE
            ON UPDATE NO ACTION
);