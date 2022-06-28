package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

@Slf4j
@Component("UserDbStorage")
@Primary
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static long id = 1;

    long userIdCounter() {
        return id++;
    }

    @Override
    public User getUser(long userId) {
        if (!doesUserExist(userId)) {
            throw new NotFoundException("Пользователь с таким ID не найден");
        }
        // выполняем запрос к базе данных.
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE user_id = ?", userId);
        // обрабатываем результат выполнения запроса
        if (rs.next()) {
            User user = new User(rs.getInt("user_id"), rs.getString("user_login"),
                    rs.getString("user_name"), rs.getString("user_email"),
                    java.sql.Date.valueOf(rs.getString("user_birthday")).toLocalDate(),
                    findUserFriends(rs.getLong("user_id")));
            log.info("Найден пользователь: {} {}", user.getId(), user.getLogin());
            return user;
        } else {
            log.info("Пользователь с идентификатором {} не найден.", userId);
            return null;
        }
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM USERS";
        return jdbcTemplate.query(sql, this::mapRowToUser);
    }

    private User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        return new User(rs.getInt("user_id"), rs.getString("user_login"),
                rs.getString("user_name"), rs.getString("user_email"),
                java.sql.Date.valueOf(rs.getString("user_birthday")).toLocalDate(),
                findUserFriends(rs.getLong("user_id")));
    }

    public Set<Long> findUserFriends(long userId) {    // TODO COMPARE TWO CONSTRUCTIONS
        String sql = "SELECT fr.second_user_id FROM users s LEFT OUTER JOIN friendship_relations fr ON s.user_id = fr.first_user_id WHERE user_id = " + userId;
        ArrayList<Long> usersList = (ArrayList<Long>) jdbcTemplate.query(sql, (rs, rowNum) ->
                (rs.getLong("second_user_id")));
        return new HashSet<Long>(usersList);
    }

    @Override
    public User create(User user) {
        user.setId(userIdCounter());
        if (user.getFriendsId() == null) user.setFriendsId(new HashSet<>());
        String sqlQuery = "INSERT INTO users(user_id, user_login, user_name, user_email, user_birthday) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                user.getId(),
                user.getLogin(),
                user.getName(),
                user.getEmail(),
                user.getBirthday());
        return user;
    }

    private boolean isFriendshipConfirmed(long userId, long friendId) {
        String sqlQuery = "SELECT count(*) FROM friendship_relations WHERE first_user_id = ? AND second_user_id = ?";
        int result = jdbcTemplate.queryForObject(sqlQuery, Integer.class, friendId, userId);
        return result == 1;
    }

    @Override
    public User put(User user) {
        if (!doesUserExist(user.getId())) {
            throw new NotFoundException("Пользователь с таким ID не найден");
        }
        if (user.getFriendsId() == null) user.setFriendsId(new HashSet<>());
        String sqlQuery = "UPDATE users SET user_login = ?, user_name = ?, user_email = ?, user_birthday = ? WHERE user_id = ?";
        jdbcTemplate.update(sqlQuery,
                user.getLogin(),
                user.getName(),
                user.getEmail(),
                user.getBirthday(),
                user.getId());
        userFriendshipRelationsUpdate(user);
        return user;
    }

    public void userFriendshipRelationsUpdate(User user) {
        if (user.getFriendsId() != null) {
            String sqlQuery = "DELETE FROM friendship_relations WHERE first_user_id = ?";
            jdbcTemplate.update(sqlQuery, user.getId());//  clearing previous records

            for (Long friendId : user.getFriendsId()) {
                String sqlQueryI = "INSERT INTO friendship_relations (first_user_id, second_user_id, friendship_status_id) " +
                        "VALUES (?, ?, ?)";
                if (isFriendshipConfirmed(user.getId(), friendId)) { // updating friendships statuses from the user's side
                    jdbcTemplate.update(sqlQueryI, user.getId(), friendId, 1);
                } else {
                    jdbcTemplate.update(sqlQueryI, user.getId(), friendId, 2);
                }
            }
            String sql = "SELECT first_user_id FROM friendship_relations WHERE second_user_id = ?"; // те кто добавил юзера в друзья сами
            List<Long> friendsAddedByUser = jdbcTemplate.query(sql, (rs, rowNum)
                    -> (rs.getLong("first_user_id")), user.getId());

            List<Long> confirmedFriendsAddedByUser = friendsAddedByUser.stream().
                    filter(x -> user.getFriendsId()
                            .contains(x)).collect(Collectors.toList());
            List<Long> notConfirmedFriendsAddedByUser = friendsAddedByUser.stream().
                    filter(x -> !(user.getFriendsId()
                            .contains(x))).collect(Collectors.toList());
            String sqlQueryI = "update FRIENDSHIP_RELATIONS set FRIENDSHIP_STATUS_ID = ? WHERE FIRST_USER_ID = ? AND SECOND_USER_ID = ?";
            confirmedFriendsAddedByUser.forEach(x -> jdbcTemplate.update(sqlQueryI, 1, x, user.getId()));
            notConfirmedFriendsAddedByUser.forEach(x -> jdbcTemplate.update(sqlQueryI, 2, x, user.getId()));
        }
    }

    @Override
    public Map<Long, User> getUsers() {
        List<User> users = findAll();
        Map<Long, User> usersMap = new HashMap<>();
        users.stream().forEach(x -> usersMap.put(x.getId(), x));
        return usersMap;
    }

    public boolean doesUserExist(long id) {
        String sqlQuery = "SELECT count(*) FROM users WHERE user_id = ?";
        long result = jdbcTemplate.queryForObject(sqlQuery, Long.class, id);
        return result == 1;
    }

}