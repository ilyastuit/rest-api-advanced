ALTER TABLE orders.gift_certificate
    ADD CONSTRAINT fk_gift_certificate
        FOREIGN KEY(gift_certificate_id)
            REFERENCES gifts.gift_certificate(id)
            ON DELETE RESTRICT
            ON UPDATE NO ACTION;

ALTER TABLE orders.gift_certificate
    ADD CONSTRAINT fk_user
        FOREIGN KEY(user_id)
            REFERENCES users.list(id)
            ON DELETE RESTRICT
            ON UPDATE NO ACTION;