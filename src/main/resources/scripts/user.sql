INSERT INTO roles (id, role)
  VALUES (1, 'ADMIN'), (2, 'USER'), (3, 'GUEST');
select setval('role_sequence', (select max(id) FROM roles), true);

-- первоначальный пароль 12345
INSERT INTO users (id, username, password)
  VALUES (1, 'admin', '$2a$10$O/lfVfh06h5BgLK4cUrGKepG6mOIwLS2SyF1TH63eFg988wBQAV46');
select setval('user_sequence', (select max(id) from users), true);

INSERT INTO users_roles (user_id, role_id)
  VALUES (1, 1), (1, 2);

INSERT INTO difficulty values (1, 'Легко'), (2, 'Ниже среднего'), (3, 'Средний уровень'),
  (4, 'Выше среднего'), (5, 'Мастер');

INSERT INTO instrument VALUES (1, 'Аккордеон', 'accordion'), (2, 'Фортепиано', 'piano'),
  (3, 'Баян', 'bayan'), (4, 'Гитара', 'guitar');
select setval('instrument_sequence', (select max(instrument_id) FROM instrument), true);