# LILIES ON LILYPOND #

## Install ##
Web-app require file `application.properties` in jar folder.
```
db.url = jdbc:postgresql://postgres/lilies
db.user = lilies
db.pass = lilies
spring.sql.init.mode=always
```

Run `database.sql` script to create schema in database.

## Adding music sheets ##
Music sheets added by calling `add.sh` script