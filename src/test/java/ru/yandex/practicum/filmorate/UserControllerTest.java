package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@SpringBootTest
@AutoConfigureMockMvc

public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    User user;

    @Autowired
    ObjectMapper mapper;

    @Test
    void test1_createValidUserResponseShouldBeOk() throws Exception {
        user = User.builder().id(1).email("student@ya.ru").login("student1234").name("Tim").birthday(LocalDate.
                of(2000,2,2)).build();
            String body = mapper.writeValueAsString(user);
            this.mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
    }

    @Test
    void test2_shouldFailDueToInvalidEmail() throws Exception {
        user = User.builder().id(1).email("studentya.ru").login("student1234").name("Tim").birthday(LocalDate.
                of(2000,2,2)).build();
        String body = mapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void test3_emailShouldNotBeEmpty() throws Exception {
        user = User.builder().id(1).email("").login("student1234").name("Tim").birthday(LocalDate.
                of(2000,2,2)).build();
        String body = mapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void test4_loginShouldNotBeEmpty() throws Exception {
        user = User.builder().id(1).email("student@ya.ru").login("").name("Tim").birthday(LocalDate.
                of(2000,2,2)).build();
        String body = mapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void test5_loginShouldNotContainSpaces() throws Exception {
        user = User.builder().id(1).email("student@ya.ru").login("student1 2 3 4").name("Tim").birthday(LocalDate.
                of(2000,2,2)).build();
        String body = mapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void test6_shouldReturnLoginInsteadOfEmptyName() throws Exception {
        user = User.builder().id(1).email("student@ya.ru").login("student1234").name("").birthday(LocalDate.
                of(2000, 2, 2)).build();
        String body = mapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON)).
                andExpect(content().string(containsString("\"name\":\"student1234\"")));
    }

    @Test
    void test7_shouldNotBirthdayBeInFuture() throws Exception {
        user = User.builder().id(1).email("student@ya.ru").login("student1234").name("Tim").birthday(LocalDate.
                of(3000,2,2)).build();
        String body = mapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}




