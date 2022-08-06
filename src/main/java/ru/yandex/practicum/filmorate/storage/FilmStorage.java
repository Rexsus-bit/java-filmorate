package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface FilmStorage {

    List<Film> findAll();

    Film create(Film film);

    Film put(Film film);

    Map<Long, Film> getFilms();

    Film getFilm(long filmId);

}
