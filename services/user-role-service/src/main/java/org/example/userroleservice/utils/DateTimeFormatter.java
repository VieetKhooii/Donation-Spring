package org.example.userroleservice.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DateTimeFormatter {
    public String formatDateTimeToString(String paymentTime) {
        java.time.format.DateTimeFormatter inputFormatter = java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime dateTime = LocalDateTime.parse(paymentTime, inputFormatter);
        java.time.format.DateTimeFormatter dateTimeFormatter = java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedPaymentTime = dateTime.format(dateTimeFormatter);
        return formattedPaymentTime;
    }
}
