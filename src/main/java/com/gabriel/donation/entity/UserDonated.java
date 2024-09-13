package com.gabriel.donation.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class UserDonated {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "donation_post_id")
    private DonationPost donationPost;

    @Column(nullable = false)
    private long amount;

    @Column(columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean anonymous = false;

    @Column(nullable = false)
    private Date donateDate;

    @Column(columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean isDeleted = false;
}
