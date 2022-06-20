package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private InMemoryUserStorage inMemoryUserStorage;

    @Autowired
private UserDbStorage filmDbStorage;

    @Autowired
    private UserService userService;

    @GetMapping
    public ArrayList<User> findAll() {
        return inMemoryUserStorage.findAll();
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable int userId) {
//        return inMemoryUserStorage.getUser(userId);
        return filmDbStorage.findUserById(userId).orElse(null);
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
        return inMemoryUserStorage.create(user);
    }

    @PutMapping
    public User put(@RequestBody User user) {
        userService.validateUser(user);
        return inMemoryUserStorage.put(user);
    }

//    @GetMapping("/s")
//    public User getUserByid(){
//        System.out.println("s");
//        return filmDbStorage.findUserById(1).get();
//    }

}
