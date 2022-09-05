package ru.practicum.shareit.item.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comments", schema = "public")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String text;

    @Column(name = "item_id")
    private Integer itemId;

    @Column(name = "author_id")
    private Integer authorId;

    private LocalDateTime created = LocalDateTime.now();

}
