CREATE SCHEMA IF NOT EXISTS movies
DEFAULT CHARACTER SET utf8
COLLATE utf8_hungarian_ci;
CREATE USER moviesUser@localhost
IDENTIFIED BY 'moviesPass';
GRANT ALL ON movies.* TO moviesUser@localhost;
USE movies;