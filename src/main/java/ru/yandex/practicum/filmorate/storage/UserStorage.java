package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {
    List<User> findAll();

    User create(User user);

    User put(User user);

    Map<Long, User> getUsers();

    User getUser(long userId);
}
