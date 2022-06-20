package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final MpaDbStorage mpaStorage;
    private final GenreDbStorage genreDbStorage;

    @Test
    public void testFindUserById() {
        userStorage.create(User.builder().id(1).login("Rex").name("Tom").email("tom@rtom.com").birthday(LocalDate.of(2000,1,1)).build());


        Optional<User> userOptional = Optional.of(userStorage.getUser(1));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testFindFilmById() {
        filmStorage.create(Film.builder().id(1).name("Vave").description("EPIC AND COOL")
                .releaseDate(LocalDate.of(2000,1,1)).duration(120)
                .userLikes(new HashSet<Long>()).genres(new TreeSet<>()).mpa(new MPA()) .build());
        Optional<Film> userOptional = Optional.of(filmStorage.getFilm(1));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testFindMPAById() {
        Optional<MPA> mpaOptional = Optional.ofNullable(mpaStorage.getMPA(1));
        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testFindGenreById() {
        Optional<FilmGenre> filmGenreOptional = Optional.ofNullable(genreDbStorage.getGenre(1));
        assertThat(filmGenreOptional)
                .isPresent()
                .hasValueSatisfying(filmGenre ->
                        assertThat(filmGenre).hasFieldOrPropertyWithValue("id", 1)
                );
    }



}

