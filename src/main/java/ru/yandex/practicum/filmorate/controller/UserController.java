package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<String, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) throws ValidationException {
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
        users.put(user.getName(), user);
        return user;
    }

    @PutMapping
    public User put(@RequestBody User user) throws ValidationException {
        if(user.getName() == null || user.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if(user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getName(), user);
        return user;
    }

}
