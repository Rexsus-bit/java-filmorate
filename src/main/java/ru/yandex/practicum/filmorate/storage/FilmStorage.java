package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.Map;

public interface FilmStorage {

    ArrayList<Film> findAll();

    Film create(Film film);

    Film put(Film film);

    public Map<Long, Film> getFilms();

}
