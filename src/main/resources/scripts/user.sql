DROP TABLE IF EXISTS roles;
CREATE TABLE roles(
	id bigint NOT NULL,
	role character varying(255),
	CONSTRAINT roles_pkey PRIMARY KEY (id));

DROP TABLE IF EXISTS users;
CREATE TABLE users(
  id bigint NOT NULL,
  password character varying(255),
  username character varying(255),
  CONSTRAINT users_pkey PRIMARY KEY (id));

DROP TABLE IF EXISTS users_roles;
CREATE TABLE users_roles(
  user_id bigint NOT NULL,
  role_id bigint NOT NULL,
  CONSTRAINT users_roles_pkey PRIMARY KEY (user_id, role_id),
  CONSTRAINT fk_1hjw31qvltj7v3wb5o31jsrd8 FOREIGN KEY (user_id)
      REFERENCES users (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_k2mq1ee4ob6uw649wgaus1ate FOREIGN KEY (role_id)
      REFERENCES roles (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)