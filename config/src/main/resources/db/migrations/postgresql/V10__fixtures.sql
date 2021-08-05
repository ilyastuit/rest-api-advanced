INSERT INTO gifts.gift_certificate (id, name, description, price, duration, create_date, last_update_date)
VALUES (1, 'Android Development', 'Android Development', 200.21, 1000, '2021-06-24 11:48:23', '2021-06-25 23:48:23');

INSERT INTO gifts.gift_certificate (id, name, description, price, duration, create_date, last_update_date)
VALUES (2, 'IOS Development', 'IOS Development', 400.21, 1000, '2021-01-24 11:48:23', '2021-03-10 10:48:23');

INSERT INTO gifts.gift_certificate (id, name, description, price, duration, create_date, last_update_date)
VALUES (3, 'Java Backend Development', 'Java Backend Development', 300.21, 340, '2020-07-24 16:48:23', '2021-05-10 14:48:23');

INSERT INTO gifts.gift_certificate (id, name, description, price, duration, create_date, last_update_date)
VALUES (4, 'Python Backend Development', 'Python Backend Development', 300.21, 400, '2021-02-09 16:48:23', '2021-04-01 08:48:23');

INSERT INTO gifts.gift_certificate (id, name, description, price, duration, create_date, last_update_date)
VALUES (5, 'English courses', 'English courses', 1500.00, 2000, '2020-10-09 10:48:23', '2021-02-01 08:48:23');

INSERT INTO gifts.gift_certificate (id, name, description, price, duration, create_date, last_update_date)
VALUES (6, 'Without tag e', 'Without tag e', 1500.00, 2000, '2020-10-09 10:48:23', '2021-02-01 08:48:23');

INSERT INTO gifts.tag (id, name) VALUES (1, 'android');
INSERT INTO gifts.tag (id, name) VALUES (2, 'ios');
INSERT INTO gifts.tag (id, name) VALUES (3, 'mobile');
INSERT INTO gifts.tag (id, name) VALUES (4, 'backend');
INSERT INTO gifts.tag (id, name) VALUES (5, 'programming');
INSERT INTO gifts.tag (id, name) VALUES (6, 'java');
INSERT INTO gifts.tag (id, name) VALUES (7, 'python');
INSERT INTO gifts.tag (id, name) VALUES (8, 'language');
INSERT INTO gifts.tag (id, name) VALUES (9, 'english');
INSERT INTO gifts.tag (id, name) VALUES (10, 'without certificate');

INSERT INTO gifts.gift_certificate_tag (id, gift_certificate_id, tag_id) VALUES (1, 1, 1);
INSERT INTO gifts.gift_certificate_tag (id, gift_certificate_id, tag_id) VALUES (2, 1, 3);
INSERT INTO gifts.gift_certificate_tag (id, gift_certificate_id, tag_id) VALUES (3, 1, 5);
INSERT INTO gifts.gift_certificate_tag (id, gift_certificate_id, tag_id) VALUES (4, 2, 2);
INSERT INTO gifts.gift_certificate_tag (id, gift_certificate_id, tag_id) VALUES (5, 2, 3);
INSERT INTO gifts.gift_certificate_tag (id, gift_certificate_id, tag_id) VALUES (6, 2, 5);
INSERT INTO gifts.gift_certificate_tag (id, gift_certificate_id, tag_id) VALUES (7, 3, 4);
INSERT INTO gifts.gift_certificate_tag (id, gift_certificate_id, tag_id) VALUES (8, 3, 5);
INSERT INTO gifts.gift_certificate_tag (id, gift_certificate_id, tag_id) VALUES (9, 3, 6);
INSERT INTO gifts.gift_certificate_tag (id, gift_certificate_id, tag_id) VALUES (10, 4, 4);
INSERT INTO gifts.gift_certificate_tag (id, gift_certificate_id, tag_id) VALUES (11, 4, 5);
INSERT INTO gifts.gift_certificate_tag (id, gift_certificate_id, tag_id) VALUES (12, 4, 7);
INSERT INTO gifts.gift_certificate_tag (id, gift_certificate_id, tag_id) VALUES (13, 5, 8);
INSERT INTO gifts.gift_certificate_tag (id, gift_certificate_id, tag_id) VALUES (14, 5, 9);

INSERT INTO users.list (id, email, password, create_date, last_update_date) VALUES (1, 'admin@mail.ru', '123', '2020-10-09 10:48:23', '2021-02-01 08:48:23');
INSERT INTO users.list (id, email, password, create_date, last_update_date) VALUES (2, 'user@mail.ru', '123', '2020-10-09 10:48:23', '2021-02-01 08:48:23');
INSERT INTO users.list (id, email, password, create_date, last_update_date) VALUES (3, 'moderator@mail.ru', '123', '2021-02-09 16:48:23', '2021-04-01 08:48:23');
INSERT INTO users.list (id, email, password, create_date, last_update_date) VALUES (4, 'newcome@mail.ru', '123', '2020-07-24 16:48:23', '2021-05-10 14:48:23');
INSERT INTO users.list (id, email, password, create_date, last_update_date) VALUES (5, 'newcome@mail1.ru', '123', '2020-07-24 16:48:23', '2021-05-10 14:48:23');

INSERT INTO orders.gift_certificate (id, price, status, user_id, gift_certificate_id, order_date, last_update_date) VALUES (1, 200.21, 'paid', 1, 1, '2020-10-09 10:48:23', '2021-02-01 08:48:23');
INSERT INTO orders.gift_certificate (id, price, status, user_id, gift_certificate_id, order_date, last_update_date) VALUES (2, 300.21, 'paid', 2, 1, '2021-10-09 10:48:23', '2021-02-01 08:48:23');
INSERT INTO orders.gift_certificate (id, price, status, user_id, gift_certificate_id, order_date, last_update_date) VALUES (3, 400.21, 'unpaid', 2, 2, '2020-10-09 10:48:23', '2021-04-01 08:48:23');
INSERT INTO orders.gift_certificate (id, price, status, user_id, gift_certificate_id, order_date, last_update_date) VALUES (4, 300.21, 'unpaid', 1, 2, '2020-10-09 10:48:23', '2021-04-01 08:48:23');
INSERT INTO orders.gift_certificate (id, price, status, user_id, gift_certificate_id, order_date, last_update_date) VALUES (5, 300.21, 'canceled', 3, 3, '2020-10-09 10:48:23', '2021-04-01 08:48:23');
INSERT INTO orders.gift_certificate (id, price, status, user_id, gift_certificate_id, order_date, last_update_date) VALUES (6, 1500.00, 'done', 4, 5, '2020-10-09 10:48:23', '2021-04-01 08:48:23');

ALTER SEQUENCE gifts.gift_certificate_id_seq RESTART WITH 7;
ALTER SEQUENCE gifts.tag_id_seq RESTART WITH 11;
ALTER SEQUENCE gifts.gift_certificate_tag_id_seq RESTART WITH 15;
ALTER SEQUENCE users.list_id_seq RESTART WITH 6;
ALTER SEQUENCE orders.gift_certificate_id_seq RESTART WITH 7;