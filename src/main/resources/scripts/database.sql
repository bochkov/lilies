-- удаление таблиц и сиквенсов
DROP TABLE IF EXISTS author CASCADE;
DROP TABLE IF EXISTS difficulty CASCADE;
DROP TABLE IF EXISTS instrument CASCADE;
DROP TABLE IF EXISTS storage CASCADE;
DROP TABLE IF EXISTS music CASCADE;
DROP TABLE IF EXISTS roles CASCADE;
DROP TABLE IF EXISTS users CASCADE;

DROP TABLE IF EXISTS author_music CASCADE;
DROP TABLE IF EXISTS difficulty_music CASCADE;
DROP TABLE IF EXISTS instrument_music CASCADE;
DROP TABLE IF EXISTS music_composer CASCADE;
DROP TABLE IF EXISTS music_instrument CASCADE;
DROP TABLE IF EXISTS music_writer CASCADE;
DROP TABLE IF EXISTS users_roles CASCADE;

DROP SEQUENCE IF EXISTS author_sequence RESTRICT;
DROP SEQUENCE IF EXISTS hibernate_sequence RESTRICT;
DROP SEQUENCE IF EXISTS instrument_sequence RESTRICT;
DROP SEQUENCE IF EXISTS music_sequence RESTRICT;
DROP SEQUENCE IF EXISTS storage_sequence RESTRICT;
DROP SEQUENCE IF EXISTS role_sequence RESTRICT;
DROP SEQUENCE IF EXISTS user_sequence RESTRICT;

-- создание сиквенсов и таблиц
CREATE SEQUENCE hibernate_sequence INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;

CREATE SEQUENCE author_sequence INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE TABLE author (
  author_id BIGINT PRIMARY KEY NOT NULL,
  first_name VARCHAR(255),
  last_name VARCHAR(255) NOT NULL,
  middle_name VARCHAR(255)
);

CREATE TABLE difficulty (
  rating INT PRIMARY KEY NOT NULL,
  name VARCHAR(255)
);

CREATE SEQUENCE instrument_sequence INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE TABLE instrument (
  instrument_id BIGINT PRIMARY KEY NOT NULL,
  name VARCHAR(255),
  slug VARCHAR(255)
);
CREATE UNIQUE INDEX instrument_slug ON instrument (slug);

CREATE SEQUENCE role_sequence INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE TABLE roles (
  id BIGINT PRIMARY KEY NOT NULL,
  role VARCHAR(255)
);

CREATE SEQUENCE user_sequence INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE TABLE users (
  id BIGINT PRIMARY KEY NOT NULL,
  password VARCHAR(255),
  username VARCHAR(255)
);

CREATE SEQUENCE storage_sequence INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE TABLE storage(
  id BIGINT PRIMARY KEY NOT NULL,
  mp3_file BYTEA,
  pdf_file BYTEA,
  src_file BYTEA
);

CREATE SEQUENCE music_sequence INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE TABLE music (
  music_id BIGINT PRIMARY KEY NOT NULL,
  name VARCHAR(255),
  subname VARCHAR(255),
  base_filename VARCHAR(255),
  src_filename VARCHAR(255),
  src_length BIGINT,
  pdf_filename VARCHAR(255),
  pdf_length BIGINT,
  mp3_filename VARCHAR(255),
  mp3_length BIGINT,
  storage_id BIGINT,
  CONSTRAINT storage_fk FOREIGN KEY (storage_id) REFERENCES storage (id)
);

-- ManyToMany : author <-> music
CREATE TABLE author_music (
  author_id BIGINT NOT NULL,
  music_id BIGINT NOT NULL,
  CONSTRAINT author_fk FOREIGN KEY (author_id) REFERENCES author (author_id),
  CONSTRAINT music_fk FOREIGN KEY (music_id) REFERENCES music (music_id)
);

-- ManyToMany : instrument <-> music
CREATE TABLE instrument_music (
  instrument_id BIGINT NOT NULL,
  music_id BIGINT NOT NULL,
  CONSTRAINT instrument_fk FOREIGN KEY (instrument_id) REFERENCES instrument (instrument_id),
  CONSTRAINT music_fk FOREIGN KEY (music_id) REFERENCES music (music_id)
);

-- ManyToMany : music <-> composer (author)
CREATE TABLE music_composer (
  music_id BIGINT NOT NULL,
  composer_id BIGINT NOT NULL,
  CONSTRAINT music_fk FOREIGN KEY (music_id) REFERENCES music (music_id),
  CONSTRAINT composer_fk FOREIGN KEY (composer_id) REFERENCES author (author_id)
);

-- ManyToMany : music <-> writer (author)
CREATE TABLE music_writer (
  music_id BIGINT NOT NULL,
  writer_id BIGINT NOT NULL,
  CONSTRAINT music_fk FOREIGN KEY (music_id) REFERENCES music (music_id),
  CONSTRAINT writer_fk FOREIGN KEY (writer_id) REFERENCES author (author_id)
);

-- ManyToOne : difficulty <-> music
CREATE TABLE difficulty_music (
  rating INT NOT NULL,
  music_id BIGINT NOT NULL,
  CONSTRAINT diff_fk FOREIGN KEY (rating) REFERENCES difficulty (rating),
  CONSTRAINT music_fk FOREIGN KEY (music_id) REFERENCES music (music_id)
);
CREATE UNIQUE INDEX music_rating ON difficulty_music (music_id);

-- ManyToMany : music <-> instrument
CREATE TABLE music_instrument (
  music_id BIGINT NOT NULL,
  instrument_id BIGINT NOT NULL,
  CONSTRAINT instrument_fk FOREIGN KEY (instrument_id) REFERENCES instrument (instrument_id),
  CONSTRAINT music_fk FOREIGN KEY (music_id) REFERENCES music (music_id)
);

-- ManyToMany : user <-> roles
CREATE TABLE users_roles (
  user_id bigint NOT NULL,
  role_id bigint NOT NULL,
  CONSTRAINT users_roles_pkey PRIMARY KEY (user_id, role_id),
  CONSTRAINT user_fk FOREIGN KEY (user_id) REFERENCES users (id) MATCH SIMPLE
  ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT role_fk FOREIGN KEY (role_id) REFERENCES roles (id) MATCH SIMPLE
  ON UPDATE CASCADE ON DELETE CASCADE
);