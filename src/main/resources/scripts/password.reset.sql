-- сброс пароля на 12345
UPDATE users SET password='$2a$10$O/lfVfh06h5BgLK4cUrGKepG6mOIwLS2SyF1TH63eFg988wBQAV46' where username='bochkov';
INSERT INTO users_roles (user_id, role_id) VALUES (1, 1), (1, 2);
commit;