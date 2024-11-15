package ru.yandex.practicum.catsgram.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.catsgram.exception.NotFoundException;

@RestControllerAdvice
public class ErrorHandler {
    @Getter
    @RequiredArgsConstructor
    public static class ErrorResponse {
        private final String error;
        private final String message;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException e) {
       return new ErrorResponse(HttpStatus.NOT_FOUND.getReasonPhrase(), e.getMessage());
    }
}
