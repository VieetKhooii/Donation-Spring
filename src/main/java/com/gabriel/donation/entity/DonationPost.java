package com.gabriel.donation.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class DonationPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;

    private Date startDate;

    private Date endDate;

    @Column(nullable = false)
    private long goalAmount;

    @Column(nullable = false)
    private long currentAmount;

    @Column(nullable = false)
    private int numberOfDonation;

    @Column(nullable = true)
    private String story;

    @ManyToOne
    @JoinColumn(name = "sponsor_id")
    private Sponsor sponsor;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "donationPost")
    private List<UserDonated> userDonateds = new ArrayList<>();

    @OneToMany(mappedBy = "donationPost")
    private List<ImageOfDonation> imageOfDonations = new ArrayList<>();

    @Column(columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean isDeleted = false;
}
