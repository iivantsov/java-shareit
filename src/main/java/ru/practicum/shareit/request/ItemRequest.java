package ru.practicum.shareit.request;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.user.model.User;

import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(length = 300)
    String description;

    @ManyToOne
    @JoinColumn(name = "requestor_id")
    User requester;

    LocalDateTime created = LocalDateTime.now();
}
