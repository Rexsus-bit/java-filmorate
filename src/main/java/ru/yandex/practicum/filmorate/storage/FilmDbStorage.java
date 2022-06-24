package ru.yandex.practicum.filmorate.storage;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Primary
@Component
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage (JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    private static long id = 1;
    private long filmIdCounter() {
        return id++;
    }

    @Override
    public List<Film> findAll() {
        String sql = "SELECT * FROM films"; //
        return jdbcTemplate.query(sql, this::mapRowToFilm);
    }

    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        return new Film(rs.getInt("film_id"), rs.getString("film_name"), rs.getString("film_description"),
                java.sql.Date.valueOf(rs.getString("release_date")).toLocalDate(),
                rs.getInt("duration"));
    }


    @Override
    public Film create(Film film) {
        film.setId(filmIdCounter());
        String sqlQuery = "insert into FILMS(FILM_ID, FILM_NAME, FILM_DESCRIPTION, RELEASE_DATE, DURATION) values (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration());
                setGenres(film);
        return film;
    }

    private void setGenres(Film film) {
//        List<Ge> genresId = film.getFilmGenresId();
//        String sqlQuery = "INSERT INTO film_genre_matches (film_id, genre_id) VALUES (?, ?)";
//        genresId.forEach(genreId -> jdbcTemplate.update(sqlQuery, genreId));
    }

//    private void setGenreMatches(Film film) {
//        List<Long> genresId = findGenreIdMatches(film.getFilmGenres());
//        String sqlQuery = "INSERT INTO film_genre_matches (GENRE_ID) VALUES (?)";
//        genresId.forEach(genreId -> jdbcTemplate.update(sqlQuery, genreId));
//    }
//
//    private List <Long> findGenreIdMatches(List<FilmGenre> filmGenres){
//            String sqlQuery = "SELECT genre_id FROM genres WHERE genre_name = ?";
//            return jdbcTemplate.query(sqlQuery, (rs, rowNum) ->
//                 rs.getLong("genre_id"), filmGenres);
//    }

    @Override
    public Film put(Film film) {
        return null;
    }

    @Override
    public Map<Long, Film> getFilms() {
        return null;
    }
}
