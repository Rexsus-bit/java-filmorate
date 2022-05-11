package ru.yandex.practicum.filmorate.controller;


import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    final int MAX_LENGTH = 200;
    final LocalDate EARLIEST_DATE  = LocalDate.of(1895, 12, 28);

    private final Map<String, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) throws ValidationException {
        if(film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if(film.getDescription().length() > MAX_LENGTH || film.getDescription().isBlank()) {
            throw new ValidationException("Превышена максимальная длинна описания фильма.");
        }
        if(film.getReleaseDate().isBefore(EARLIEST_DATE)) {
            throw new ValidationException("Дата не может быть раньше чем 28 декабря 1895 года.");
        }
        if(film.getDuration().toNanos() <= 0) {
            throw new ValidationException(film.getDuration() + "У фильма должна быть положительная продолжительность.");
        }
        films.put(film.getName(), film);
        return film;
    }

    @PutMapping
    public Film put(@RequestBody Film film) throws ValidationException {
        if(film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        films.put(film.getName(), film);

        return film;
    }
}
