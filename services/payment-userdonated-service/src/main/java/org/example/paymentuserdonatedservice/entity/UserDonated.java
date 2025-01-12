package org.example.paymentuserdonatedservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.paymentuserdonatedservice.payload.PaymentMethod;

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

    @Column(nullable = false)
    private int userId;

    @Column(nullable = false)
    private int donationPostId;

    @Column(nullable = false)
    private long amount;

    @Column(columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean anonymous = false;

    @Column(nullable = false)
    private Date donateDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Column(columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean isDeleted = false;

    public interface UserDonatedProjection {
        String getUsername();
        long getAmount();
        boolean isAnonymous();
    }
}
