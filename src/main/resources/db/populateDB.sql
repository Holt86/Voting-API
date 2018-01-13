DELETE FROM user_roles;
DELETE FROM user_voter;
DELETE FROM dish;
DELETE FROM users;
DELETE FROM restaurant;
ALTER SEQUENCE global_seq RESTART WITH 100;

INSERT INTO users (name, email, password) VALUES
  ('User1', 'user1@yandex.ru', '$2a$10$kpr5QRyU2aj/eCjqweVxreGScNqM82vUvRCORLjAZopo.Kxp5B2S2'),
  ('User2', 'user2@yandex.ru', '$2a$10$OS.c29wTqAmFWM7fsMe6SuV/1gMmXHQq0oXFi/l9ofs.5Z0Dw5tmS'),
  ('Admin', 'admin@mail.ru', '$2a$10$GDeQFdS0EmOUJtskC4PMp.84FOLWrRiTx3KB32u0t909ZVXc6aOAi');

INSERT INTO restaurant (name) VALUES
  ('mama roma'),
  ('grill master'),
  ('carols');

INSERT INTO menu (date, restaurant_id) VALUES
  ('2017-12-30', 103),
  ('2017-12-30', 104),
  (now(), 103),
  (now(), 104);

INSERT INTO dish (name, price, menu_id) VALUES
  ('пицца паперони', 35000, 106),
  ('лазанья с ветчиной', 46050, 106),
  ('спагетини', 47037, 106),
  ('пицца салями', 37000, 108),
  ('паста с грибами', 48020, 108),
  ('ребрышки барбекю', 58000, 107),
  ('шашлык бараний', 57025, 107),
  ('форель гриль', 84000, 109),
  ('ребрышки барбекю', 58531, 109);

INSERT INTO user_roles (user_id, role) VALUES
  (100, 'ROLE_USER'),
  (101, 'ROLE_USER'),
  (102, 'ROLE_ADMIN'),
  (102, 'ROLE_USER');

INSERT INTO user_voter (date_vote, user_id, menu_id) VALUES
  ('2017-12-30', 100, 106),
  ('2017-12-30', 101, 106),
  ('2017-12-30', 102, 107),
  (now(), 101, 108);



