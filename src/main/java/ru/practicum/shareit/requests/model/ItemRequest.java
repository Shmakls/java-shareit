package ru.practicum.shareit.requests.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table (name = "requests", schema = "public")
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String description;

    @Column (name = "requestor_id")
    private Integer requestorId;

    private LocalDateTime created = LocalDateTime.now();



}
