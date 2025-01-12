package org.example.postimageservice.service;

import com.gabriel.donation.dto.MailBody;

public interface EmailService {
    public void sendSimpleMessage(MailBody mailBody);
}
