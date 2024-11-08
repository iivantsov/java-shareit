package ru.practicum.shareit.item.model;

import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.shareit.user.model.User;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(length = 300)
    String text;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "item_id")
    Item item;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "author_id")
    User author;

    @CreationTimestamp
    LocalDateTime created;
}
