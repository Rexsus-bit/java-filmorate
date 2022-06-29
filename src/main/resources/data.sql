DELETE FROM film_genre_matches;
DELETE FROM friendship_relations;
DELETE FROM friendship_status;
DELETE FROM genres;
DELETE FROM user_likes;
DELETE FROM films;
DELETE FROM mpa_rating;
DELETE FROM users;

INSERT INTO mpa_rating (mpa_id, mpa_name)
VALUES
    (1, 'G'),
    (2, 'PG'),
    (3, 'PG-13'),
    (4, 'R'),
    (5, 'NC-17');

INSERT INTO genres (genre_id, genre_name)
VALUES
    (1, 'Комедия'),
    (2, 'Драма'),
    (3, 'Мультфильм'),
    (4, 'Триллер'),
    (5, 'Документальный'),
    (6, 'Боевик');

INSERT INTO friendship_status (friendship_status_id, friendship_status)
VALUES
    (1, 'confirmed'),
    (2, 'not confirmed');


