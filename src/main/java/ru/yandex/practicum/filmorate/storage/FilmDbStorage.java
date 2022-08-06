package ru.yandex.practicum.filmorate.storage;

import lombok.Data;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Primary
@Component
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static long id = 1;

    public static void setId(long id) {
        FilmDbStorage.id = id;
    }

    private long filmIdCounter() {
        return id++;
    }

    @Override
    public List<Film> findAll() {
        String sql = "SELECT * FROM films"; //
        List<Film> films = jdbcTemplate.query(sql, this::mapRowToFilm);
        return films;
    }

    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        Film film = Film.builder()
                .id(rs.getLong("film_id"))
                .name(rs.getString("film_name"))
                .description(rs.getString("film_description"))
                .releaseDate(java.sql.Date.valueOf(rs.getString("release_date")).toLocalDate())
                .duration(rs.getInt("duration"))
                .build();
        setMPA(film);
        setLikes(film);
        setGenres(film);
        return film;
    }

    private void setMPA(Film film) {
        String sqlQuery = "SELECT f.mpa_id, m.mpa_name FROM films f LEFT OUTER JOIN mpa_rating m ON f.mpa_id = m.mpa_id WHERE film_id = ?";
        MPA mpa = jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> MPA.builder().id(rs.getInt("mpa_id")).name(/*MpaStatus.valueOf(*/rs.getString("mpa_name"))/*)*/.build(), film.getId());
        film.setMpa(mpa);
    }

    private void setLikes(Film film) {
        String sqlQuery = "SELECT count(*) FROM user_likes WHERE film_id = ?";
        int likesNumber = jdbcTemplate.queryForObject(sqlQuery, Integer.class, film.getId());
        if (likesNumber > 0) {
            String sqlQuery1 = "SELECT user_id FROM user_likes WHERE film_id = ?";
            List<Long> likesList = jdbcTemplate.queryForList(sqlQuery1, Long.class, film.getId());
            film.setUserLikes(new HashSet<>(likesList));
        } else {
            film.setUserLikes(new HashSet<>());
        }
    }

    private void setGenres(Film film) {
        String sqlQuery = "SELECT count(*) FROM film_genre_matches WHERE film_id = ?";
        int genresQuantity = jdbcTemplate.queryForObject(sqlQuery, Integer.class, film.getId());
        if (genresQuantity > 0) {
            String sqlQuery2 = "SELECT fg.genre_id, g.genre_name FROM film_genre_matches fg LEFT OUTER JOIN genres g ON fg.genre_id = g.genre_id WHERE film_id = ?";

            List<FilmGenre> filmGenres = jdbcTemplate.query(sqlQuery2, this::mapRowToFilmGenre, film.getId());
            film.setGenres(new TreeSet<>(filmGenres));
        }
    }

    private FilmGenre mapRowToFilmGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return FilmGenre.builder()
                .id(resultSet.getInt("genre_id"))
                .name(FilmGenreTypes.valueOf(resultSet.getString("genre_name")))
                .build();
    }

    @Override
    public Film create(Film film) {
        if (film.getUserLikes() == null) {
            film.setUserLikes(new HashSet<>());
        }
        if (film.getMpa() == null) {
            throw new ValidationException("MPA фильма не найден");
        } else {
            film.setId(filmIdCounter());
            String sqlQuery = "INSERT INTO films(film_id, film_name, film_description, release_date, duration, mpa_id) VALUES (?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(sqlQuery,
                    film.getId(),
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId());
        }
        insertGenres(film);
        return film;
    }

    private void insertGenres(Film film) {
        if (film.getGenres() != null) {
            Set<FilmGenre> genres = film.getGenres();
            String sqlQuery = "INSERT INTO film_genre_matches (film_id, genre_id) VALUES (?, ?)";
            genres.forEach(genre -> jdbcTemplate.update(sqlQuery, film.getId(), genre.getId()));
        }
    }

    @Override
    public Film put(Film film) {
        if (!doesFilmExist(film.getId())) throw new NotFoundException("Фильм с таким ID не найден");
        if (film.getUserLikes() == null) {
            film.setUserLikes(new HashSet<>());
        }
        if (film.getMpa() != null) {
            String sqlQuery = "UPDATE films SET film_name = ?, film_description = ?, release_date = ?, duration = ?, mpa_id= ? WHERE film_id = ?";
            jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId());
        } else {
            String sqlQuery = "UPDATE films SET film_name = ?, film_description = ?, release_date = ?, duration = ? WHERE film_id = ?";
            jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getId());
        }
        updateGenres(film);
        return film;
    }

    private void updateGenres(Film film) {
        String sqlQuery = "DELETE FROM film_genre_matches WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
        Set<FilmGenre> genres = film.getGenres();
        if (genres != null) {
            String sqlQuery2 = "INSERT INTO film_genre_matches (film_id, genre_id) VALUES (?, ?)";
            genres.forEach(genre -> jdbcTemplate.update(sqlQuery2, film.getId(), genre.getId()));
        }
    }

    private boolean doesFilmExist(long filmId) {
        String sqlQuery = "SELECT count(*) FROM films WHERE film_id = ?";
        long result = jdbcTemplate.queryForObject(sqlQuery, Long.class, filmId);
        return result == 1;
    }

    @Override
    public Map<Long, Film> getFilms() {
        List<Film> films = findAll();
        return films.stream().collect(Collectors.toMap(Film::getId, film -> film));
    }

    @Override
    public Film getFilm(long filmId) {
        Map<Long, Film> films = getFilms();
        if (!films.containsKey(filmId)) throw new NotFoundException("Фильм с таким ID не найден");
        return films.get(filmId);
    }
}
