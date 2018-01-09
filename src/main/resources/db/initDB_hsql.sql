DROP TABLE user_roles IF EXISTS;
DROP TABLE user_voter IF EXISTS;
DROP TABLE dish IF EXISTS;
DROP TABLE menu IF EXISTS;
DROP TABLE restaurant IF EXISTS;
DROP TABLE users IF EXISTS;
DROP SEQUENCE global_seq IF EXISTS;

CREATE SEQUENCE GLOBAL_SEQ AS INTEGER START WITH 100;

CREATE TABLE restaurant
(
  id   INTEGER GENERATED BY DEFAULT AS SEQUENCE GLOBAL_SEQ PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);
CREATE UNIQUE INDEX restaurant_unique_name_idx ON restaurant (name);

CREATE TABLE menu
(
  id            INTEGER GENERATED BY DEFAULT AS SEQUENCE GLOBAL_SEQ PRIMARY KEY,
  date          DATE DEFAULT now() NOT NULL,
  restaurant_id INTEGER            NOT NULL,
  CONSTRAINT date_name_restaurant_idx UNIQUE (date, restaurant_id),
  CONSTRAINT fk_menu_to_restaurant_id FOREIGN KEY (restaurant_id) REFERENCES restaurant (id) ON DELETE CASCADE
);

CREATE TABLE dish
(
  id      INTEGER GENERATED BY DEFAULT AS SEQUENCE GLOBAL_SEQ PRIMARY KEY,
  name    VARCHAR(255) NOT NULL,
  price   INTEGER NOT NULL,
  menu_id INTEGER NOT NULL,
  CONSTRAINT date_name_menu_idx UNIQUE (name, menu_id),
  CONSTRAINT fk_dish_to_menu_id FOREIGN KEY (menu_id) REFERENCES menu (id) ON DELETE CASCADE
);

CREATE TABLE users
(
  id         INTEGER GENERATED BY DEFAULT AS SEQUENCE GLOBAL_SEQ PRIMARY KEY,
  name       VARCHAR(255)                 NOT NULL,
  email      VARCHAR(255)                 NOT NULL,
  password   VARCHAR(255)                 NOT NULL,
  registered TIMESTAMP DEFAULT now() NOT NULL,
  enabled    BOOLEAN DEFAULT TRUE       NOT NULL
);
CREATE UNIQUE INDEX users_unique_email_idx ON users (email);

CREATE TABLE user_roles
(
  user_id INTEGER NOT NULL,
  role    VARCHAR(255) NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
  CONSTRAINT user_id_role_idx UNIQUE (user_id, role)
);

CREATE TABLE user_voter
(
  id            INTEGER GENERATED BY DEFAULT AS SEQUENCE GLOBAL_SEQ PRIMARY KEY,
  date_vote     DATE DEFAULT now() NOT NULL,
  user_id       INTEGER            NOT NULL,
  menu_id INTEGER            NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
  FOREIGN KEY (menu_id) REFERENCES menu (id) ON DELETE CASCADE,
  CONSTRAINT user_id_date_vote_idx UNIQUE (user_id, date_vote)
)