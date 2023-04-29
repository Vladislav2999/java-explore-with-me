package ru.practicum.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.dto.UpdateEventUserRequest;
import ru.practicum.events.service.EventServicePrivate;
import ru.practicum.requests.dto.EventRequestStatusUpdateRequest;
import ru.practicum.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.requests.dto.ParticipationRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
public class EventControllerPrivate {
    private final EventServicePrivate eventServicePriv;

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getByUserId(
            @PathVariable("userId") @PositiveOrZero Long userId,
            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Запрос событий созданных пользователем с id - " + userId);
        return new ResponseEntity<>(eventServicePriv.getEventsByUserId(userId, from, size),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EventFullDto> create(
            @PathVariable("userId") @PositiveOrZero Long userId,
            @RequestBody @Valid NewEventDto newEventDto) {
        log.info("Запрос добавления нового события от пользователя с id - " + userId);
        return new ResponseEntity<>(eventServicePriv.createEvent(userId, newEventDto),HttpStatus.CREATED);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getFullInfoByEventIdAndUserId(
            @PathVariable("userId") @PositiveOrZero Long userId,
            @PathVariable("eventId") @PositiveOrZero Long eventId) {
        log.info("Запрос информации о событии с id - {} созданным пользователем с id - {}", eventId, userId);
        return new ResponseEntity<>(eventServicePriv.getEventByIdAndUserId(userId, eventId),HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity <EventFullDto> updateByUserIdAndEventId(
            @PathVariable("userId") @PositiveOrZero Long userId,
            @PathVariable("eventId") @PositiveOrZero Long eventId,
            @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        log.info("Запрос изменения события с id - {} добавленного пользователем с id - {}", eventId, userId);
        return new ResponseEntity<>(eventServicePriv.updateEventByIdAndUserId(userId, eventId, updateEventUserRequest),HttpStatus.OK);
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getRequestByUserIdAndEventId(
            @PathVariable("userId") @PositiveOrZero Long userId,
            @PathVariable("eventId") @PositiveOrZero Long eventId) {
        log.info("Запрос информации о запросах на участие в событии с id - {} ползователя с id - {}", eventId, userId);
        return new ResponseEntity<>(eventServicePriv.getRequestByUserIdAndEventId(userId, eventId),HttpStatus.OK);
    }

    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> updateStatusRequestByUserIdAndEventId(
            @PathVariable("userId") @PositiveOrZero Long userId,
            @PathVariable("eventId") @PositiveOrZero Long eventId,
            @RequestBody EventRequestStatusUpdateRequest statusUpdateRequest) {
        log.info("Изменение статуса заявок на участие в событии с id - {} пользователя с id - {} ", eventId, userId);
        return new ResponseEntity<>(eventServicePriv.updateStatusRequestByUserIdAndEventId(userId, eventId,
                statusUpdateRequest),HttpStatus.OK);
    }
}
