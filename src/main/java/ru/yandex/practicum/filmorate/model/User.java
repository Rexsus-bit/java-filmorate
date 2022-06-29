package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class User {
    private long id;
    private String login;
    private String name;
    private String email;
    private LocalDate birthday;
    private Set<Long> friendsId;

    public User(long id, String login, String name, String email, LocalDate birthday, Set<Long> friendsId) {
        this.id = id;
        this.login = login;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
        this.friendsId = friendsId;
    }

    public User(long id, String login, String name, String email, LocalDate birthday) {
        this.id = id;
        this.login = login;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
    }

    public User(String login, String name, String email) {
        this.login = login;
        this.name = name;
        this.email = email;
    }

    public User() {
    }
}


