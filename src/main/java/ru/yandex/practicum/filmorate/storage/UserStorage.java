package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;
import java.util.ArrayList;
import java.util.Map;

public interface UserStorage {
    ArrayList<User> findAll();

    User create(User user);

    User put(User user);

    Map<Long, User> getUsers();
}
