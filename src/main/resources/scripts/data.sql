INSERT INTO roles (id, role)
  VALUES (1, 'ADMIN'), (2, 'USER'), (3, 'GUEST');

-- первоначальный пароль 12345
INSERT INTO users (id, username, password)
  VALUES (1, 'admin', '$2a$10$O/lfVfh06h5BgLK4cUrGKepG6mOIwLS2SyF1TH63eFg988wBQAV46');

INSERT INTO users_roles (user_id, role_id)
  VALUES (1, 1), (1, 2);