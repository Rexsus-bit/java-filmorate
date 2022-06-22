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
    ('Comedy'),
    ('Drama'),
    ('Cartoon'),
    ('Thriller'),
    ('Documentary'),
    ('Action_movie');

INSERT INTO FRIENDSHIP_STATUS (FRIENDSHIP_STATUS)
VALUES
    ('confirmed'),
    ('not confirmed');

INSERT INTO USERS (user_login,
                   user_name,
                   user_email,
                   user_birthday)
VALUES ('Rex', 'Tom', 'tom@soyer@travel.com', '1946-08-20'),
       ('Rex2', 'Tom', 'tom@soyer@travel.com', '1946-08-20'),
       ('Rex3', 'Tom', 'tom@soyer@travel.com', '1946-08-20'),
       ('Rex4', 'Tom', 'tom@soyer@travel.com', '1946-08-20');


INSERT INTO FRIENDSHIP_REALATIONS (FIRST_USER_ID, SECOND_USER_ID, FRIENDSHIP_STATUS_ID)
VALUES (1, 2, 1),
       (2, 1, 1),
       (1, 3, 1),
       (1, 4, 1);



