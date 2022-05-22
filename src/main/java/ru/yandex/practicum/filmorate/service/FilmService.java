package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.IncorrectLikeException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {

    FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    void toLikeFilm(Long userId, Long filmId){
        Set<Long> userLikes = filmStorage.getFilms().get(filmId).getUserLikes();
        if (userLikes.contains(userId)) throw new IncorrectLikeException("Вы уже поставили лайк!");
        else {
            userLikes.add(userId);
        }
    }

    void unlikeFilm(Long userId, Long filmId){
        Set<Long> userLikes = filmStorage.getFilms().get(filmId).getUserLikes();
        if (!userLikes.contains(userId)) throw new IncorrectLikeException("Лайк у фильма отсутствует - его нельзя убрать!");
        else {
            userLikes.remove(userId);
        }
    }

    List<Film> getTenTopFilms(){
        Map<Long, Film> films = filmStorage.getFilms();
        return films.values().stream()
                .sorted((p0, p1) -> compare(p0, p1))
                .limit(10)
                .collect(Collectors.toList());
    }

    private int compare(Film p0, Film p1) {
        return p0.getUserLikes().size() - p1.getUserLikes().size(); //TODO проверить порядок сортировки
    }

}
