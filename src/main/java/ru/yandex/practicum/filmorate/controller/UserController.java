package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserStorage userStorage;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> findAll() {
        return userStorage.findAll();
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable int userId) {
        return userStorage.getUser(userId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addToFriendList(@PathVariable long id, @PathVariable long friendId) {
        userService.addToFriendList(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFromFriendList(@PathVariable long id, @PathVariable long friendId) {
        userService.removeFromFriendList(id, friendId);
    }

    @GetMapping("{id}/friends")
    public List<User> getUserFriendsList(@PathVariable long id) {
        return userService.getUserFriendsList(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriendsList(@PathVariable long id, @PathVariable long otherId) {
        return userService.getCommonFriendsList(id, otherId);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        userService.validateUser(user);
        return userStorage.create(user);
    }

    @PutMapping
    public User put(@RequestBody User user) {
        userService.validateUser(user);
        return userStorage.put(user);
    }


}
