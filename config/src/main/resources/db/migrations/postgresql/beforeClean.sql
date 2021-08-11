DO
$$
    BEGIN
        IF EXISTS(
                   SELECT schema_name
                   FROM information_schema.schemata
                   WHERE schema_name = 'orders'
               ) AND EXISTS(
                   SELECT table_name, table_schema
                   FROM information_schema.tables
                   WHERE table_name = 'gift_certificate'
                     AND table_schema = 'orders'
               )
        THEN
            EXECUTE 'ALTER TABLE orders.gift_certificate DROP CONSTRAINT IF EXISTS fk_gift_certificate';
            EXECUTE 'ALTER TABLE orders.gift_certificate DROP CONSTRAINT IF EXISTS fk_user';
        END IF;

    END
$$;