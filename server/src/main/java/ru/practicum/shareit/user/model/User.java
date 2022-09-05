package ru.practicum.shareit.user.model;

import lombok.*;
import javax.persistence.*;

@Data
@Entity
@Table(name = "users", schema = "public")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String email;

    private String name;

}
