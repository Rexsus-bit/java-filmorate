package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.IncorrectLikeException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {

    FilmStorage filmStorage;
    UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void likeFilm(Long userId, Long filmId){
        Set<Long> userLikes = filmStorage.getFilms().get(filmId).getUserLikes();
        if (userLikes.contains(userId)) throw new IncorrectLikeException("Вы уже поставили лайк!");
        else {
            userLikes.add(userId);
        }
    }

    public void unlikeFilm(Long filmId, Long userId){
        Set<Long> userLikes = filmStorage.getFilms().get(filmId).getUserLikes();
        if (!userStorage.getUsers().containsKey(userId)) throw new NotFoundException("Пользователь с таким Id отсутствует!");
        if (!userLikes.contains(userId)) throw new NotFoundException("Лайк у фильма отсутствует - его нельзя убрать!");
        if (!filmStorage.getFilms().containsKey(filmId)) throw new NotFoundException("Фильм с таким Id отсутствует");
        else {
            userLikes.remove(userId);
        }
    }

    public List<Film> getTopFilms(int count){
        Map<Long, Film> films = filmStorage.getFilms();
        return films.values().stream()
                .sorted((p0, p1) -> compare(p0, p1))
                .limit(count)
                .collect(Collectors.toList());
    }

    private int compare(Film p0, Film p1) {
        return (p0.getUserLikes().size() - p1.getUserLikes().size()) * -1;
    }

}
