package ru.yandex.practicum.filmorate.exceptions;

public class IncorrectLikeException extends RuntimeException {
    public IncorrectLikeException(String message) {
        super(message);
    }
}