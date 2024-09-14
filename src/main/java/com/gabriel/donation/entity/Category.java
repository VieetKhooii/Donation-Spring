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
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true, length = 25)
    private String name;

    @OneToMany(mappedBy = "category")
    private List<DonationPost> donationPosts = new ArrayList<>();

    @Column(columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean isDeleted = false;
}
