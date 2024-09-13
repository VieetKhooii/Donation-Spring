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

    @Column(nullable = true, length = 35)
    private String description;

    @Column(nullable = true)
    private String url;

    @ManyToOne
    @JoinColumn(name = "donation_post_id")
    private DonationPost donationPost;
}
