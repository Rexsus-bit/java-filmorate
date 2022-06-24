--
-- INSERT INTO USERS (user_login, user_name, user_email, user_birthday) VALUES ('Tom', 'Soyer', 'post@post.ru', '1946-08-20');

INSERT INTO MPA_rating (MPA_NAME)
VALUES
    ('G'),
    ('PG'),
    ('PG_13'),
    ('R'),
    ('NC_17');


-- INSERT INTO MPA_rating (MPA_NAME)
-- SELECT
--     ('G'),
--     ('PG'),
--     ('PG_13'),
--     ('R'),
--     ('NC_17')
-- WHERE ;


INSERT INTO GENRES (GENRE_NAME)
VALUES
    ('Комедия'),
    ('Драма'),
    ('Мультфильм'),
    ('Триллер'),
    ('Документальный'),
    ('Боевик');

INSERT INTO FRIENDSHIP_STATUS (FRIENDSHIP_STATUS)
VALUES
    ('confirmed'),
    ('not confirmed');

INSERT INTO USERS (USER_ID, USER_LOGIN,
                   USER_NAME,
                   USER_EMAIL,
                   USER_BIRTHDAY)
VALUES (1, 'Rex', 'Tom', 'tom@soyer@travel.com', '1946-08-20'),
       (2, 'Rex2', 'Tom', 'tom@soyer@travel.com', '1946-08-20'),
       (3, 'Rex3', 'Tom', 'tom@soyer@travel.com', '1946-08-20'),
       (4, 'Rex4', 'Tom', 'tom@soyer@travel.com', '1946-08-20');


INSERT INTO FRIENDSHIP_RELATIONS (FIRST_USER_ID, SECOND_USER_ID, FRIENDSHIP_STATUS_ID)
VALUES (1, 2, 1),
       (2, 1, 1),
       (1, 3, 1),
       (1, 4, 1);



