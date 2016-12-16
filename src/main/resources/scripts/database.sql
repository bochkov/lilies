-- удаление таблиц и сиквенсов
DROP TABLE IF EXISTS author CASCADE;
DROP TABLE IF EXISTS difficulty CASCADE;
DROP TABLE IF EXISTS instrument CASCADE;
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

-- создание таблиц
CREATE TABLE author (
  author_id BIGINT PRIMARY KEY NOT NULL,
  first_name VARCHAR(255),
  last_name VARCHAR(255) NOT NULL,
  middle_name VARCHAR(255)
);

CREATE TABLE difficulty (
  rating INTEGER PRIMARY KEY NOT NULL,
  name VARCHAR(255)
);

CREATE TABLE instrument (
  instrument_id BIGINT PRIMARY KEY NOT NULL,
  name VARCHAR(255),
  slug VARCHAR(255)
);
CREATE UNIQUE INDEX instrument_index ON instrument (slug);

CREATE TABLE music (
  music_id BIGINT PRIMARY KEY NOT NULL,
  base_filename VARCHAR(255),
  mp3_file OID,
  mp3_length BIGINT,
  mp3_filename VARCHAR(255),
  name VARCHAR(255),
  pdf_file OID,
  pdf_length BIGINT,
  pdf_filename VARCHAR(255),
  src_file OID,
  src_length BIGINT,
  src_filename VARCHAR(255),
  subname VARCHAR(255)
);

CREATE TABLE roles (
  id BIGINT PRIMARY KEY NOT NULL,
  role VARCHAR(255)
);

CREATE TABLE users (
  id BIGINT PRIMARY KEY NOT NULL,
  password VARCHAR(255),
  username VARCHAR(255)
);

CREATE TABLE author_music (
  author_id BIGINT NOT NULL REFERENCES author (author_id),
  music_id BIGINT NOT NULL REFERENCES music (music_id)
);

CREATE TABLE difficulty_music (
  rating INTEGER REFERENCES difficulty (rating),
  music_id BIGINT PRIMARY KEY NOT NULL REFERENCES music (music_id)
);

CREATE TABLE instrument_music (
  instrument_id BIGINT NOT NULL REFERENCES instrument (instrument_id),
  music_id BIGINT NOT NULL REFERENCES music (music_id)
);

CREATE TABLE music_composer (
  music_id BIGINT NOT NULL REFERENCES music (music_id),
  composer_id BIGINT NOT NULL REFERENCES author (author_id)
);
CREATE TABLE music_instrument (
  music_id BIGINT NOT NULL REFERENCES music (music_id),
  instrument_id BIGINT NOT NULL REFERENCES instrument (instrument_id)
);

CREATE TABLE music_writer (
  music_id BIGINT NOT NULL REFERENCES music(music_id),
  writer_id BIGINT NOT NULL REFERENCES author (author_id)
);

CREATE TABLE users_roles (
  user_id BIGINT NOT NULL REFERENCES users (id),
  role_id BIGINT NOT NULL REFERENCES roles (id),
  CONSTRAINT users_roles_pkey PRIMARY KEY (user_id, role_id)
);