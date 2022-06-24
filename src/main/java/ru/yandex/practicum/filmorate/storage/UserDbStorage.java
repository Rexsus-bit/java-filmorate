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
public class UserDbStorage implements UserStorage{

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage (JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    private static long id = 1;

    long userIdCounter() {
        return id++;
    }

    @Override
    public User getUser(long userId) {
        if (!doesUserExist(userId)){
            throw new NotFoundException("Пользователь с таким ID не найден");
        }
        // выполняем запрос к базе данных.
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT * FROM USERS WHERE USER_ID = ?", userId);
        // обрабатываем результат выполнения запроса
        if(rs.next()) {
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
        String sql = "SELECT * FROM USERS"; // TODO COMPARE TWO CONSTRUCTIONS
        return jdbcTemplate.query(sql, this::mapRowToUser);
    }

    private User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        return new User(rs.getInt("user_id"), rs.getString("user_login"),
                rs.getString("user_name"), rs.getString("user_email"),
                java.sql.Date.valueOf(rs.getString("user_birthday")).toLocalDate(),
            findUserFriends(rs.getLong("user_id")));
    }

    public Set<Long> findUserFriends(long userId) {    // TODO COMPARE TWO CONSTRUCTIONS
        String sql = "SELECT FR.SECOND_USER_ID FROM USERS S LEFT OUTER JOIN FRIENDSHIP_RELATIONS FR ON S.USER_ID = FR.FIRST_USER_ID WHERE USER_ID = " + userId;
        ArrayList<Long> usersList = (ArrayList<Long>) jdbcTemplate.query(sql, (rs, rowNum) ->
                 (rs.getLong("SECOND_USER_ID")));
        return new HashSet<Long>(usersList);
    }

    @Override
    public User create(User user) {
        user.setId(userIdCounter());
        String sqlQuery = "insert into USERS(USER_ID, USER_LOGIN, USER_NAME, USER_EMAIL, USER_BIRTHDAY) values (?, ?, ?, ?, ?)";
            jdbcTemplate.update(sqlQuery,
                    user.getId(),
                    user.getLogin(),
                    user.getName(),
                   user.getEmail(),
                   user.getBirthday());
//            saveFriends(user); // TODO а нужно ли добавлять друзей при создании? наверное нет, тут ведь еще только создается профиль
        return user;
    }
//    private void saveFriends(User user){
//        for (Long friendId : user.getFriendsId()) {
//            String sqlQuery = "insert into FRIENDSHIP_RELATIONS (FIRST_USER_ID, SECOND_USER_ID, FRIENDSHIP_STATUS_ID) values (?, ?, ?)";
//
//            jdbcTemplate.update(sqlQuery,
//                    user.getId(),
//                    friendId,
//                    isFriendshipConfirmed(user.getId(), friendId));
//        }
//    }

    private boolean isFriendshipConfirmed(long userId, long friendId) {
//
            String sqlQuery = "select count(*) from FRIENDSHIP_RELATIONS where FIRST_USER_ID = ? AND SECOND_USER_ID = ?";
            //noinspection ConstantConditions: return value is always an int, so NPE is impossible here
            int result = jdbcTemplate.queryForObject(sqlQuery, Integer.class, friendId, userId);
            return result == 1;
    }


    @Override
    public User put(User user) {
        if (!doesUserExist(user.getId())){
            throw new NotFoundException("Пользователь с таким ID не найден");
        }
        String sqlQuery = "update USERS set /*USER_ID = ?,*/ USER_LOGIN = ?, USER_NAME = ?, USER_EMAIL = ?, USER_BIRTHDAY = ? WHERE USER_ID = ?";
        jdbcTemplate.update(sqlQuery,
              /*  user.getId(),*/
                user.getLogin(),
                user.getName(),
                user.getEmail(),
                user.getBirthday(),
                user.getId());
        userFriendshipRelationsUpdate(user);
        return user;
    }

    private void userFriendshipRelationsUpdate(User user) { // updates friendship relation statuses between user and friends
        if(user.getFriendsId() != null) {
        String sqlQuery = "delete from FRIENDSHIP_RELATIONS where FIRST_USER_ID = ?";
        jdbcTemplate.update(sqlQuery, user.getId());//  clearing previous records

        for (Long friendId : user.getFriendsId()) { // insert new friends
            String sqlQueryI = "insert into FRIENDSHIP_RELATIONS (FIRST_USER_ID, SECOND_USER_ID, FRIENDSHIP_STATUS_ID) " +
                    "values (?, ?, ?)";
            if (isFriendshipConfirmed(user.getId(), friendId)) { // updating friendships statuses from the user's side
                jdbcTemplate.update(sqlQueryI, user.getId(), friendId, 1);
            }
            else {
                jdbcTemplate.update(sqlQueryI, user.getId(), friendId, 2);
            }
        }
            String sql = "SELECT FIRST_USER_ID FROM FRIENDSHIP_RELATIONS WHERE SECOND_USER_ID = ?"; // те кто добавил юзера в друзья сами
            List<Long> friendsAddedByUser = jdbcTemplate.query(sql, (rs, rowNum)
                    -> (rs.getLong("FIRST_USER_ID")), user.getId());

            List<Long> confirmedFriendsAddedByUser= friendsAddedByUser.stream().
                    filter(x -> user.getFriendsId()
                           .contains(x)).collect(Collectors.toList());
            List<Long> notConfirmedFriendsAddedByUser= friendsAddedByUser.stream().
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
        String sqlQuery = "select count(*) from USErS where USER_ID = ?";
        long result = jdbcTemplate.queryForObject(sqlQuery, Long.class, id);
        return result == 1;
    }

}