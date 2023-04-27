package ru.practicum.compilations.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.NewCompilationDto;
import ru.practicum.compilations.dto.UpdateCompilationDto;
import ru.practicum.compilations.service.CompilationServiceAdmin;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
@Slf4j
public class CompilationControllerAdmin {
    private final CompilationServiceAdmin compilationServiceAdmin;

    @PostMapping
    public ResponseEntity <CompilationDto> create(
            @RequestBody @Valid NewCompilationDto newCompilationDto) {
        log.info("Запрос создания подборки от администратора");
        return new ResponseEntity<>(compilationServiceAdmin.createCompilation(newCompilationDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable("compId") @Positive Long compId) {
        log.info("Запрос удаления подборки от администратора - " + compId);
        compilationServiceAdmin.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationDto> update(
            @PathVariable("compId") @Positive Long compId,
            @RequestBody @Valid UpdateCompilationDto dto) {
        log.info("Запрос обновления подборки от администратора - " + compId);
        return new ResponseEntity<>(compilationServiceAdmin.updateCompilation(compId, dto),HttpStatus.OK);
    }
}
