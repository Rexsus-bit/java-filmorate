package ru.yandex.practicum.filmorate.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.IncorrectLikeException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private JdbcTemplate jdbcTemplate;
    private FilmStorage filmStorage;
    private UserStorage userStorage;

    public FilmService(JdbcTemplate jdbcTemplate, FilmStorage filmStorage, UserStorage userStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void likeFilm(Long userId, Long filmId) {
        Set<Long> userLikes = getFilmById(filmId).getUserLikes();
        if (userLikes.contains(userId)) throw new IncorrectLikeException("Вы уже поставили лайк!");
        else {
            String sqlQuery = "INSERT INTO user_likes (user_id, film_id) VALUES (?, ?)";
            jdbcTemplate.update(sqlQuery, userId, filmId);
        }
    }

    public void unlikeFilm(Long filmId, Long userId) {
        Set<Long> userLikes = getFilmById(filmId).getUserLikes();
        if (!userStorage.getUsers().containsKey(userId))
            throw new NotFoundException("Пользователь с таким Id отсутствует!");
        if (!userLikes.contains(userId)) throw new NotFoundException("Лайк у фильма отсутствует - его нельзя убрать!");
        if (!filmStorage.getFilms().containsKey(filmId)) throw new NotFoundException("Фильм с таким Id отсутствует");
        else {
            String sqlQuery = "DELETE FROM user_likes WHERE USER_ID = ? AND FILM_ID = ?";
            jdbcTemplate.update(sqlQuery, userId, filmId);
        }
    }

    public List<Film> getTopFilms(int count) {
        Map<Long, Film> films = filmStorage.getFilms();
        return films.values().stream()
                .sorted((p0, p1) -> compare(p0, p1))
                .limit(count)
                .collect(Collectors.toList());
    }

    private int compare(Film p0, Film p1) {
        return (p0.getUserLikes().size() - p1.getUserLikes().size()) * -1;
    }

    private Film getFilmById(long filmId) { // OK

        return filmStorage.getFilms().get(filmId);
    }

    public void validateFilm(Film film) {
        final int MAX_LENGTH = 200;
        final LocalDate EARLIEST_DATE = LocalDate.of(1895, 12, 28);

        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if (film.getDescription().length() > MAX_LENGTH || film.getDescription().isBlank()) {
            throw new ValidationException("Превышена максимальная длинна описания фильма.");
        }
        if (film.getReleaseDate().isBefore(EARLIEST_DATE)) {
            throw new ValidationException("Дата не может быть раньше чем 28 декабря 1895 года.");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("У фильма должна быть положительная продолжительность.");
        }
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film getFilm(long filmId) {
        return filmStorage.getFilm(filmId);
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film put(Film film) {
        return filmStorage.put(film);
    }
}
