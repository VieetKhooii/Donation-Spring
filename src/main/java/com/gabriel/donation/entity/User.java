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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private long balance;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Payment> payments = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserDonated> userDonateds = new ArrayList<>();

    @Column(columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean isDeleted = false;
}
