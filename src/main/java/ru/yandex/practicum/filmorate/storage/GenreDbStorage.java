package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.FilmGenreTypes;

import java.util.List;

@Component
public class GenreDbStorage {

    JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<FilmGenre> findAll() {
        String sqlQuery = "SELECT genre_id, genre_name FROM genres";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> FilmGenre.builder().id(rs.getInt("genre_id")).name(FilmGenreTypes.valueOf(rs.getString("genre_name"))).build());
    }

    public FilmGenre getGenre(int id) {
        String sqlQuery1 = "SELECT count(*) FROM genres WHERE genre_id = ?";
        int result = jdbcTemplate.queryForObject(sqlQuery1, Integer.class, id);
        if (result < 1) throw new NotFoundException("Значения с таким ID нет");

        String sqlQuery = "SELECT genre_id, genre_name FROM genres WHERE genre_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> FilmGenre.builder().id(rs.getInt("genre_id")).name(FilmGenreTypes.valueOf(rs.getString("genre_name"))).build(), id);
    }
}
