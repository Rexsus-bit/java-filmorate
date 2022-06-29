package ru.yandex.practicum.filmorate.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

@Component
public class DataBaseCleaner {
    private final JdbcTemplate jdbcTemplate;

    FilmDbStorage filmDbStorage;
    UserDbStorage userDbStorage;

    @Autowired
    public DataBaseCleaner(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void clean() {
        String sql =
                "DELETE FROM users;" +
                        "DELETE FROM films;" +
                        "DELETE FROM film_genre_matches;" +
                        "DELETE FROM friendship_relations;" +
                        "ALTER TABLE films ALTER COLUMN film_id RESTART START WITH 1;" +
                        "ALTER TABLE film_genre_matches ALTER COLUMN index RESTART START WITH 1;" +
                        "ALTER TABLE friendship_relations ALTER COLUMN relation_id RESTART START WITH 1;" +
                        "ALTER TABLE user_likes  ALTER COLUMN like_ID RESTART START WITH 1;";
        jdbcTemplate.update(sql);
        filmDbStorage.setId(1);
        userDbStorage.setId(1);



    }
}