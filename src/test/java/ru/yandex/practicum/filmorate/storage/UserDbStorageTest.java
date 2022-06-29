package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.DataBaseCleaner;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {

    private final UserStorage userStorage;
    private final DataBaseCleaner cleaner;

    @BeforeEach
    public void beforeEach() {
        cleaner.clean();
        userStorage.create(User.builder().id(1).login("Rex").name("Tom").email("tom@rtom.com").birthday(LocalDate.of(2000,1,1)).build());
    }

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
    void testUpdateUser() {
        userStorage.put(User.builder().id(1).login("Rex").name("Tomas").email("tom@rtom.com").birthday(LocalDate.of(2000,1,1)).build());

        Optional<User> userOptional = Optional.of(userStorage.getUser(1));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "Tomas")
                );
    }

    @Test
    void testGetAllUsers() {
        Optional<User> userOptional = Optional.of(userStorage.findAll().get(0));
        assertThat(userOptional)
                .isPresent()
                .get().isEqualTo(userStorage.getUser(1));
    }
}