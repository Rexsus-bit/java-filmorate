package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    InMemoryUserStorage inMemoryUserStorage;

    @GetMapping
    public ArrayList<User> findAll() {
        return inMemoryUserStorage.findAll();
    }

    @GetMapping("{userId}")
    public User getUser(@PathVariable("userId") String userId){
        return inMemoryUserStorage.getUsers().get(userId);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return inMemoryUserStorage.create(user);
    }

    @PutMapping
    public User put(@RequestBody User user) {
        return inMemoryUserStorage.put(user);
    }

}
