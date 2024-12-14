package org.example.userroleservice.service;

import com.gabriel.donation.dto.MailBody;

public interface EmailService {
    public void sendSimpleMessage(MailBody mailBody);
}
