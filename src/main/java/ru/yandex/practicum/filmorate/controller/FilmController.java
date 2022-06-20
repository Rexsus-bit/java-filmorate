package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;


@RestController
@RequestMapping("/films")
public class FilmController {


    private FilmStorage filmStorage;
    private FilmService filmService;

    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    @GetMapping("/{filmId}")
    public Film getFilm(@PathVariable long filmId) {
        return filmStorage.getFilm(filmId);
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        filmService.validateFilm(film);
        return filmStorage.create(film);
    }

    @PutMapping
    public Film put(@RequestBody Film film) {
        filmService.validateFilm(film);
        return filmStorage.put(film);
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
