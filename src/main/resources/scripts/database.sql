-- удаление таблиц и сиквенсов
DROP TABLE IF EXISTS author CASCADE;
DROP TABLE IF EXISTS difficulty CASCADE;
DROP TABLE IF EXISTS instrument CASCADE;
DROP TABLE IF EXISTS music CASCADE;
DROP TABLE IF EXISTS storage CASCADE;

DROP TABLE IF EXISTS music_composer CASCADE;
DROP TABLE IF EXISTS music_instrument CASCADE;
DROP TABLE IF EXISTS music_writer CASCADE;

-- создание таблиц
CREATE TABLE author (
  author_id   SERIAL PRIMARY KEY NOT NULL,
  last_name   VARCHAR(255)       NOT NULL,
  first_name  VARCHAR(255),
  middle_name VARCHAR(255)
);

CREATE TABLE difficulty (
  rating INTEGER PRIMARY KEY NOT NULL,
  name   VARCHAR(255)
);
INSERT INTO difficulty values (1, 'Легко'),
  (2, 'Ниже среднего'), (3, 'Средний уровень'),
  (4, 'Выше среднего'), (5, 'Мастер');

CREATE TABLE instrument (
  instrument_id SERIAL PRIMARY KEY NOT NULL,
  name          VARCHAR(255),
  slug          VARCHAR(255)
);
CREATE UNIQUE INDEX instrument_index
  ON instrument (slug);
INSERT INTO instrument VALUES (1, 'Аккордеон', 'accordion'),
  (2, 'Фортепиано', 'piano'), (3, 'Баян', 'bayan'), (4, 'Гитара', 'guitar');

CREATE TABLE storage (
  storage_id SERIAL PRIMARY KEY NOT NULL,
  filename   VARCHAR(255)       NOT NULL,
  src        OID,
  pdf        OID,
  mp3        OID
) WITHOUT OIDS;

CREATE TABLE music (
  music_id   SERIAL PRIMARY KEY NOT NULL,
  name       VARCHAR(255),
  subname    VARCHAR(255),
  difficulty INTEGER REFERENCES difficulty (rating),
  storage_id BIGINT REFERENCES storage (storage_id)
);

CREATE TABLE music_composer (
  music_id    BIGINT NOT NULL REFERENCES music (music_id),
  composer_id BIGINT NOT NULL REFERENCES author (author_id)
);

CREATE TABLE music_instrument (
  music_id      BIGINT NOT NULL REFERENCES music (music_id),
  instrument_id BIGINT NOT NULL REFERENCES instrument (instrument_id)
);

CREATE TABLE music_writer (
  music_id  BIGINT NOT NULL REFERENCES music (music_id),
  writer_id BIGINT NOT NULL REFERENCES author (author_id)
);
