package org.example.postimageservice.entity;

import com.gabriel.donation.entity.ImageOfDonation;

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

    @Column(nullable = true, length = 1000)
    private String story;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @OneToMany(mappedBy = "donationPost")
    private List<UserDonated> userDonateds = new ArrayList<>();

    @OneToMany(mappedBy = "donationPost")
    private List<ImageOfDonation> imageOfDonations = new ArrayList<>();

    @Column(columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean isDeleted = false;
}
