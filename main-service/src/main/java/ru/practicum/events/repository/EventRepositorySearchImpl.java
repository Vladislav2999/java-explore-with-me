package ru.practicum.events.repository;


import org.springframework.stereotype.Repository;
import ru.practicum.events.model.Event;
import ru.practicum.events.state.State;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class EventRepositorySearchImpl implements EventRepositorySearch {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Event> publicSearch(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                    LocalDateTime rangeEnd, long from, int size) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = cb.createQuery(Event.class);
        Root<Event> event = query.from(Event.class);
        Predicate predicate = cb.conjunction();

        if (text != null && !text.isEmpty()) {
            Predicate annotation = cb.like(cb.lower(event.get("annotation")), "%" + text.toLowerCase() + "%");
            Predicate description = cb.like(cb.lower(event.get("description")), "%" + text.toLowerCase() + "%");
            Predicate hasText = cb.or(description, annotation);
            predicate = cb.and(predicate, hasText);
        }

        query.select(event).where(predicate);
        return entityManager.createQuery(query).setMaxResults(size).getResultList();
    }

    @Override
    public List<Event> adminSearch(List<Long> users, List<String> states, List<Long> categories,
                                   LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = cb.createQuery(Event.class);
        Root<Event> event = query.from(Event.class);
        Predicate predicate = cb.conjunction();

        if (users != null) {
            Predicate initiators = event.get("initiator").in(users);
            predicate = cb.and(predicate, initiators);
        }
        if (states != null) {
            List<State> stateList = states.stream()
                    .map(State::valueOf)
                    .collect(Collectors.toList());
            Predicate inStates = event.get("state").in(stateList);
            predicate = cb.and(predicate, inStates);
        }
        if (categories != null) {
            Predicate category = event.get("category").in(categories);
            predicate = cb.and(predicate, category);
        }
        if (rangeStart != null) {
            Predicate start = cb.greaterThan(event.get("eventDate"), rangeStart);
            predicate = cb.and(predicate, start);
        }
        if (rangeEnd != null) {
            Predicate end = cb.lessThan(event.get("eventDate"), rangeEnd);
            predicate = cb.and(predicate, end);
        }

        Predicate id = cb.greaterThanOrEqualTo(event.get("id"), from);
        predicate = cb.and(predicate, id);
        query.select(event).where(predicate);
        return entityManager.createQuery(query).setMaxResults(size).getResultList();
    }
}
