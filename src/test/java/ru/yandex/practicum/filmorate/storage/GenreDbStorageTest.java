package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDbStorageTest {

    private final GenreDbStorage genreDbStorage;

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