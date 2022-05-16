package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<String, User> users = new HashMap<>();

    @GetMapping
    public ArrayList<User> findAll() {
        return new ArrayList<User>(users.values());
    }

    @PostMapping
    public User create(@RequestBody User user) {
        validate(user);
        users.put(user.getName(), user);
        return user;
    }

    @PutMapping
    public User put(@RequestBody User user) {
        validate(user);
        users.put(user.getName(), user);
        return user;
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
