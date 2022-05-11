package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private static long id = 1;

    private long filmIdCounter() {
        return id++;
    }

    public ArrayList<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    public Film create(Film film) {
        film.setId(filmIdCounter());
        if (film.getUserLikes() == null) {
            film.setUserLikes(new HashSet<>());
        }
        films.put(film.getId(), film);
        return film;
    }

    public Film put(Film film) {
        if (!films.containsKey(film.getId())) throw new NotFoundException("Фильм с таким ID не найден");
        if (film.getUserLikes() == null) film.setUserLikes(new HashSet<>());
        films.put(film.getId(), film);
        return film;
    }

    public Film getFilm(long filmId) {
        if (!films.containsKey(filmId)) throw new NotFoundException("Фильм с таким ID не найден");
        return films.get(filmId);
    }

    public Map<Long, Film> getFilms() {
        return films;
    }
}
