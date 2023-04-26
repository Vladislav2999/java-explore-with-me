package ru.practicum.events.model;

import lombok.*;
import ru.practicum.categories.model.Category;
import ru.practicum.events.state.State;
import ru.practicum.users.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 2000, nullable = false)
    private String annotation;
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private Category category;
    @Column
    private Long confirmedRequests;
    @Column(length = 7000, nullable = false)
    private String description;
    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;
    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;
    @ManyToOne
    @JoinColumn(name = "initiator_id", referencedColumnName = "id", nullable = false)
    private User initiator;
    @Embedded
    private Location location;
    @Column(nullable = false)
    private Boolean paid;
    @Column(name = "participant_limit")
    private Long participantLimit;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Column(name = "request_moderation", nullable = false)
    private Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    private State state;
    @Column(nullable = false, unique = true)
    private String title;
    @Column
    private Long views;
}



