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
public class Sponsor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String avatar;

    @Column(nullable = false, unique = true, length = 25)
    private String name;

    @Column(nullable = true, length = 35)
    private String description;

    @OneToMany(mappedBy = "sponsor")
    private List<DonationPost> donationPosts = new ArrayList<>();

    @Column(columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean isDeleted = false;
}
