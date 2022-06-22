package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import ru.yandex.practicum.filmorate.model.User;

@Slf4j
@Component("UserDbStorage")
@Primary
public class UserDbStorage implements UserStorage{

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage (JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User getUser(long id) {
        // выполняем запрос к базе данных.
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT * FROM USERS WHERE USER_ID = ?", id);
        // обрабатываем результат выполнения запроса
        if(rs.next()) {
            User user = new User(rs.getInt("user_id"), rs.getString("user_login"),
                    rs.getString("user_name"), rs.getString("user_email"),
                    java.sql.Date.valueOf(rs.getString("user_birthday")).toLocalDate(),
                    findUserFriends(rs.getLong("user_id")));
            log.info("Найден пользователь: {} {}", user.getId(), user.getLogin());
            return user;
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            return null;
        }
    }
    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM USERS"; // TODO COMPARE TWO CONSTRUCTIONS
        return jdbcTemplate.query(sql, this::mapRowToUser);
    }

//    public ArrayList<User> findAll() {    // TODO COMPARE TWO CONSTRUCTIONS
//        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM USERS");
//        String sql = "SELECT * FROM USERS";
//        return (ArrayList<User>) jdbcTemplate.query(sql, (rs, rowNum) ->
//                new User(rs.getInt("user_id"),
//                        rs.getString("user_login"),
//                        rs.getString("user_name"),
//                        rs.getString("user_email"),
//                        java.sql.Date.valueOf(rs.getString("user_birthday")).toLocalDate()));
//    }
    private User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        return new User(rs.getInt("user_id"), rs.getString("user_login"),
                rs.getString("user_name"), rs.getString("user_email"),
                java.sql.Date.valueOf(rs.getString("user_birthday")).toLocalDate(),
            findUserFriends(rs.getLong("user_id")));
    }

    public Set<Long> findUserFriends(long userId) {    // TODO COMPARE TWO CONSTRUCTIONS
        String sql = "SELECT FR.SECOND_USER_ID FROM USERS S LEFT OUTER JOIN FRIENDSHIP_REALATIONS FR ON S.USER_ID = FR.FIRST_USER_ID WHERE USER_ID = " + userId;
        ArrayList<Long> usersList = (ArrayList<Long>) jdbcTemplate.query(sql, (rs, rowNum) ->
                 (rs.getLong("SECOND_USER_ID")));
        return new HashSet<Long>(usersList);
    }

    @Override
    public User create(User user) {
            String sqlQuery = "insert into USERS(USER_LOGIN, USER_NAME, USER_EMAIL, USER_BIRTHDAY) values (?, ?, ?, ?)";
            jdbcTemplate.update(sqlQuery,
                    user.getLogin(),
                    user.getName(),
                   user.getEmail(),
                   user.getBirthday());
        return user;
    }

    @Override
    public User put(User user) {
        String sqlQuery = "update USERS set USER_LOGIN = ?, USER_NAME = ?, USER_EMAIL = ?, USER_BIRTHDAY = ? WHERE USER_ID = ?";
        jdbcTemplate.update(sqlQuery,
                user.getLogin(),
                user.getName(),
                user.getEmail(),
                user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public Map<Long, User> getUsers() {
        List<User> users = findAll();
        Map<Long, User> usersMap = new HashMap<>();
        users.stream().forEach(x -> usersMap.put(x.getId(), x));
        return usersMap;
    }
}