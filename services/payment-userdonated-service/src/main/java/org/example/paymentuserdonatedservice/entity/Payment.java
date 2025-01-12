package org.example.paymentuserdonatedservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int  id;

    @Column(nullable = false)
    private long amount;

//    @Column(nullable = false)
    private Date transaction_date;

    @Column(nullable = false)
    private int userId;

    @Column(columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean isDeleted = false;
}
