package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    @Autowired
    private InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    private FilmService filmService;

    @GetMapping
    public List<Film> findAll() {
        return inMemoryFilmStorage.findAll();
    }

    @GetMapping("/{filmId}")
    public Film getUser(@PathVariable long filmId) {
        return inMemoryFilmStorage.getFilm(filmId);
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        filmService.validateFilm(film);
        return inMemoryFilmStorage.create(film);
    }

    @PutMapping
    public Film put(@RequestBody Film film) {
        filmService.validateFilm(film);
        return inMemoryFilmStorage.put(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeFilm(@PathVariable long id, @PathVariable long userId) {
        filmService.likeFilm(userId, id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void unlikeFilm(@PathVariable long id, @PathVariable long userId) {
        filmService.unlikeFilm(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam(defaultValue = "10") int count) {
        if (count != 10) {
            List<Film> s = filmService.getTopFilms(count);
            return filmService.getTopFilms(count);
        } else return filmService.getTopFilms(10);
    }

}
