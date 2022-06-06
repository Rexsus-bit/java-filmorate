package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private static long id = 1;

    long userIdCounter() {
        return id++;
    }

    public ArrayList<User> findAll() {
        return new ArrayList<User>(users.values());
    }

    public User create(User user) {
        user.setId(userIdCounter());
        if (user.getFriendsId() == null) user.setFriendsId(new HashSet<>());
        users.put(user.getId(), user);
        return user;
    }

    public User put(User user) {
        if (!users.containsKey(user.getId())) throw new NotFoundException("Пользователь с таким ID не найден");
        if (user.getFriendsId() == null) user.setFriendsId(new HashSet<>());
        users.put(user.getId(), user);
        return user;
    }

    public Map<Long, User> getUsers() {
        return users;
    }

    public User getUser(long userId) {
        if (!users.containsKey(userId)) throw new NotFoundException("Пользователь с таким ID не найден");
        return getUsers().get(userId);
    }
}
