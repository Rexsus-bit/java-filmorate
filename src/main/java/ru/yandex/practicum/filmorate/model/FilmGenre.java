package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FilmGenre implements Comparable<FilmGenre> {
    Integer id;
    FilmGenreTypes name;

    public FilmGenre(Integer id, FilmGenreTypes name) {
        this.id = id;
        this.name = name;
    }

    public FilmGenre() {
    }

    @Override
    public int compareTo(FilmGenre o) {
        return this.getId() - o.getId();
    }
}
