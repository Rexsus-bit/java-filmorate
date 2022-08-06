package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

@Component
public class MpaDbStorage {

    private JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<MPA> findAll() {
        String sqlQuery = "SELECT mpa_id, mpa_name FROM mpa_rating";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> MPA.builder().id(rs.getInt("mpa_id")).name(/*MpaStatus.valueOf(*/rs.getString("mpa_name"))/*)*/.build());

    }

    public MPA getMPA(int id) {
        String sqlQuery1 = "SELECT count(*) FROM mpa_rating WHERE mpa_id = ?";
        int result = jdbcTemplate.queryForObject(sqlQuery1, Integer.class, id);
        if (result < 1) throw new NotFoundException("Значения с таким ID нет");

        String sqlQuery = "SELECT mpa_id, mpa_name FROM mpa_rating WHERE mpa_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> MPA.builder().id(rs.getInt("mpa_id")).name(rs.getString("mpa_name")).build(), id);
    }

}
