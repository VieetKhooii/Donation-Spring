package org.example.paymentuserdonatedservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class PaymentDTO {
    @JsonProperty("payment_id")
    private int paymentId;

    private long amount;

    @JsonProperty("transaction_date")
    private Date transactionDate;

    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("is_deleted")
    private boolean isDeleted;
}
