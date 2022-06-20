package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import ru.yandex.practicum.filmorate.model.User;

@Slf4j
@Component
@Primary
public class UserDbStorage implements UserStorage{

    private final JdbcTemplate jdbcTemplate;


    public UserDbStorage (JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ArrayList<User> findAll() {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM USERS");


        return null;
    }

    @Override
    public User create(User user) {
        return null;
    }

    @Override
    public User put(User user) {
        return null;
    }

    @Override
    public Map<Long, User> getUsers() {
        return null;
    }


    public Optional<User> findUserById(int id) {
        // выполняем запрос к базе данных.
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM USERS WHERE USER_ID = ?", id);

        // обрабатываем результат выполнения запроса
        if(userRows.next()) {
            User user = new User(
                    userRows.getInt("user_id"),
                    userRows.getString("user_login"),
                    userRows.getString("user_name"),
                    userRows.getString("user_email"),
                    null,
                    null
            );
            log.info("Найден пользователь: {} {}", user.getId(), user.getLogin());
            return Optional.of(user);
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }
}