package ru.practicum.shareit.item.model;

import ru.practicum.shareit.request.ItemRequest;

import jakarta.persistence.*;
import lombok.*;

import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.model.User;

@Entity
@Table(name = "items")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(length = 50)
    String name;

    @Column(length = 300)
    String description;

    /**
     * Booking availability status
     */
    @Column(name = "is_available")
    Boolean available;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    User owner;

    /**
     * Link to another User's Item Request (if it was created)
     */
    @OneToOne
    @JoinColumn(name = "request_id")
    ItemRequest request;
}
