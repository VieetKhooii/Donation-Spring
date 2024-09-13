package com.gabriel.donation.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int  id;

    @Column(nullable = false, unique = true, length = 25)
    private String name;

    @Column(nullable = true, length = 35)
    private String description;

    @OneToMany(mappedBy = "role")
    private List<User> users = new ArrayList<>();
}
