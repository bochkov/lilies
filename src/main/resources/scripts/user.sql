INSERT INTO roles (id, role) VALUES (1, 'ADMIN'), (2, 'USER'), (3, 'GUEST');

-- первоначальный пароль 12345
INSERT INTO users (id, username, password) VALUES (1, 'bochkov', '$2a$10$O/lfVfh06h5BgLK4cUrGKepG6mOIwLS2SyF1TH63eFg988wBQAV46');
INSERT INTO users_roles (user_id, role_id) VALUES (1, 1), (1, 2);

INSERT INTO difficulty values (1, 'Легко'), (2, 'Ниже среднего'), (3, 'Средний уровень'), (4, 'Выше среднего'), (5, 'Мастер');
INSERT INTO instrument VALUES (1, 'Аккордеон', 'accordion'), (2, 'Фортепиано', 'piano'), (3, 'Баян', 'bayan'), (4, 'Гитара', 'guitar');