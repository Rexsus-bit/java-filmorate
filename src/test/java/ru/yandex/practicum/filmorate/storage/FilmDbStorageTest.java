package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.util.DataBaseCleaner;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)


class FilmDbStorageTest {

    private final FilmStorage filmStorage;
    private final DataBaseCleaner cleaner;


    @BeforeEach
    public void beforeEach() {
        cleaner.clean();
        filmStorage.create(Film.builder().id(1).name("Vave").description("EPIC AND COOL")
                .releaseDate(LocalDate.of(2000, 1, 1)).duration(120)
                .userLikes(new HashSet<Long>()).genres(new TreeSet<>()).mpa(new MPA()).build());
    }

    @Test
    public void testFindFilmById() {
        Optional<Film> userOptional = Optional.of(filmStorage.getFilm(1));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    void testUpdateFilm() {
        filmStorage.put(Film.builder().id(1).name("Roger").description("EPIC AND COOL")
                .releaseDate(LocalDate.of(2000, 1, 1)).duration(120)
                .userLikes(new HashSet<>()).genres(new TreeSet<>()).mpa(new MPA()).build());
        Optional<Film> filmOptional = Optional.of(filmStorage.getFilm(1));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "Roger")
                );
    }

    @Test
    void testGetAllFilms() {

        Optional<Film> filmOptional = Optional.of(filmStorage.findAll().get(0));
        assertThat(filmOptional)
                .isPresent()
                .get().isEqualTo(filmStorage.getFilm(1));

    }



}