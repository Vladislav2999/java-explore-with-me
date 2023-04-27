package ru.practicum.requests.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.service.RequestServicePrivate;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
public class RequestControllerPrivate {
    private final RequestServicePrivate requestServicePrivate;

    @GetMapping
    public List<ParticipationRequestDto> getByUserId(
            @PathVariable("userId") @PositiveOrZero Long userId) {

        log.info("Запрос получения информации о заявках пользователя с id - {} на участие в чужих событиях", userId);
        return requestServicePrivate.getRequestByUserId(userId);
    }

    @PostMapping
    public ResponseEntity<ParticipationRequestDto> createByUserId(
            @PathVariable("userId") @PositiveOrZero Long userId,
            @RequestParam(name = "eventId") @PositiveOrZero Long eventId) {

        log.info("Запрос на добавление запроса на участие от пользователя с id - {} на участие в событии с id - {}",
                userId, eventId);
        return new ResponseEntity<>(requestServicePrivate.createRequestByUserId(userId, eventId),HttpStatus.CREATED);
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancellationByUserIdAndRequestId(
            @PathVariable("userId") @PositiveOrZero Long userId,
            @PathVariable("requestId") @PositiveOrZero Long requestId) {
        log.info("Запрос отмены запроса на участие в событии с id - {} пользователя с id - {}",requestId, userId);
        return new ResponseEntity<>(requestServicePrivate.cancelRequestByIdAndUserId(userId, requestId),HttpStatus.OK);
    }
}
