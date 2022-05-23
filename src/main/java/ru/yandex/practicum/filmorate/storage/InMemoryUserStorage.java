package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage{

    private final Map<Long, User> users = new HashMap<>();
    private static long id = 1;

    long userIdCounter(){
        return id++;
    }

    public ArrayList<User> findAll() {
        return new ArrayList<User>(users.values());
    }

    public User create(User user) {
        validate(user);
        user.setId(userIdCounter());
        if (user.getFriendsId() == null) user.setFriendsId(new HashSet<>());
        users.put(user.getId(), user);
        return user;
    }

    public User put(User user) {
        if (!users.containsKey(user.getId())) throw new NotFoundException("Пользователь с таким ID не найден");
        validate(user);
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

    private void validate(User user){
        if(user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if(user.getLogin().contains(" ") || user.getLogin() == null || user.getLogin().isBlank() ) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }
        if(user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
        if(user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
