INSERT INTO roles (id, role)
  VALUES (1, 'ADMIN'), (2, 'USER'), (3, 'GUEST');

INSERT INTO users (id, username, password)
  VALUES (1, 'admin', '123');

INSERT INTO users_roles (user_id, role_id)
  VALUES (1, 1), (1, 2);