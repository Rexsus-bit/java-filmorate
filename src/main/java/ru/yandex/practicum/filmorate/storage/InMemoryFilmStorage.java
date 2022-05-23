package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final int MAX_LENGTH = 200;
    private final LocalDate EARLIEST_DATE = LocalDate.of(1895, 12, 28);

    private final Map<Long, Film> films = new HashMap<>();
    private static long id = 1;

    private long filmIdCounter(){
        return id++;
    }

    public ArrayList<Film> findAll() {
        return new ArrayList<>(films.values());
    }


    public Film create(Film film) {
        validate(film);
        film.setId(filmIdCounter());
        if (film.getUserLikes() == null)film.setUserLikes(new HashSet<>());
        films.put(film.getId(), film);
        return film;
    }

    public Film put(Film film) {
        if (!films.containsKey(film.getId())) throw new NotFoundException("Фильм с таким ID не найден");
        validate(film);
        if (film.getUserLikes() == null)film.setUserLikes(new HashSet<>());
        films.put(film.getId(), film);
        return film;
    }

    public Film getFilm(long filmId) {
        if (!films.containsKey(filmId)) throw new NotFoundException("Пользователь с таким ID не найден");
        return films.get(filmId);
    }

    public Map<Long, Film> getFilms() {
        return films;
    }

    private void validate(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if (film.getDescription().length() > MAX_LENGTH || film.getDescription().isBlank()) {
            throw new ValidationException("Превышена максимальная длинна описания фильма.");
        }
        if (film.getReleaseDate().isBefore(EARLIEST_DATE)) {
            throw new ValidationException("Дата не может быть раньше чем 28 декабря 1895 года.");
        }
        if (film.getDuration()/*.toNanos()*/ <= 0) {
            throw new ValidationException("У фильма должна быть положительная продолжительность.");
        }
    }
}
