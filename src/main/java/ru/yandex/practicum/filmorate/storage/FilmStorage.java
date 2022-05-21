package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;

public interface FilmStorage {

    ArrayList<Film> findAll();

    Film create(Film film);

    Film put(Film film);

}
