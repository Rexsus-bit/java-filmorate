package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Data
@Builder
public class Film {
    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Set<Long> userLikes;
    private TreeSet<FilmGenre> genres;
    private MPA mpa;

    public Film(long id, String name, String description, LocalDate releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public Film(long id, String name, String description, LocalDate releaseDate, int duration, Set<Long> userLikes, TreeSet<FilmGenre> filmGenresId, MPA mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.userLikes = userLikes;
        this.genres = filmGenresId;
        this.mpa = mpa;
    }

    public Film() {
    }
}
