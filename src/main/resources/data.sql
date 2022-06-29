-- DELETE FROM film_genre_matches;
-- DELETE FROM friendship_relations;
-- DELETE FROM friendship_status;
-- DELETE FROM genres;
-- DELETE FROM user_likes;
-- DELETE FROM films;
-- DELETE FROM mpa_rating;
-- DELETE FROM users;

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

-- INSERT INTO USERS (USER_ID, USER_LOGIN,
--                    USER_NAME,
--                    USER_EMAIL,
--                    USER_BIRTHDAY)
-- VALUES (1, 'Rex', 'Tom', 'tom@soyer@travel.com', '1946-08-20'),
--        (2, 'Rex2', 'Tom', 'tom@soyer@travel.com', '1946-08-20'),
--        (3, 'Rex3', 'Tom', 'tom@soyer@travel.com', '1946-08-20'),
--        (4, 'Rex4', 'Tom', 'tom@soyer@travel.com', '1946-08-20');
--

-- INSERT INTO films (film_name,
--                    film_description,
--                    release_date,
--                    duration,
--                    mpa_id)
-- VALUES ('v','d','1999-01-01', 120, 1);
--
-- DELETE FROM films;
--
-- DBCC CHECKIDENT ('films', RESEED, 0);

-- ALTER TABLE films ALTER COLUMN film_id RESTART START WITH 1;
--
-- -- INSERT INTO FRIENDSHIP_RELATIONS (FIRST_USER_ID, SECOND_USER_ID, FRIENDSHIP_STATUS_ID)
-- -- VALUES (1, 2, 2),
-- -- --        (2, 1, 1),
-- -- --        (1, 3, 1),
-- --        (1, 4, 2);


