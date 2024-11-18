package com.gabriel.donation.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class ImageOfDonation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int  id;

    @Column(nullable = true )
    private String description;

    @Column(nullable = true)
    private String url;

    @ManyToOne
    @JoinColumn(name = "donation_post_id")
    private DonationPost donationPost;

    @Column(columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean isDeleted = false;
}
