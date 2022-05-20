package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;
import java.util.ArrayList;
import java.util.Map;

public interface UserStorage {

    @GetMapping
    public ArrayList<User> findAll();

    @PostMapping
    public User create(@RequestBody User user);

    @PutMapping
    public User put(@RequestBody User user);

    Map<Long, User> getUsers();

}
